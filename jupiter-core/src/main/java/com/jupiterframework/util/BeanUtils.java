package com.jupiterframework.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.esotericsoftware.kryo.Kryo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BeanUtils {
    private BeanUtils() {
    }

    /** Map<source class,<target class , BeanCopier>> */
    private static final ConcurrentHashMap<Class<?>, ConcurrentHashMap<Class<?>, BeanCopier>> cache =
            new ConcurrentHashMap<>();

    private static final ThreadLocal<Kryo> KRYO = ThreadLocal.withInitial(Kryo::new);

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final FastJsonConfig FASTJSON_CONFIG = new FastJsonConfig();
    private static final SerializerFeature[] SERIALIZER_FEATURES =
            new SerializerFeature[] { SerializerFeature.BrowserSecure,
                                      SerializerFeature.DisableCircularReferenceDetect };
    static {
        FASTJSON_CONFIG.setDateFormat(DATE_FORMAT);
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        serializeConfig.put(Void.class, ToStringSerializer.instance);
        serializeConfig.put(Void.TYPE, ToStringSerializer.instance);
        serializeConfig.put(TypeReferenceLong.class, ToStringSerializer.instance);
        serializeConfig.put(Date.class, new SimpleDateFormatSerializer(DATE_FORMAT));
        FASTJSON_CONFIG.setSerializerFeatures(SERIALIZER_FEATURES);
        FASTJSON_CONFIG.setSerializeConfig(serializeConfig);
        FASTJSON_CONFIG.setFeatures(Feature.AllowSingleQuotes);
    }

    public static class TypeReferenceLong extends TypeReference<Long> {

    }


    /** 浅拷贝，对象属性都是基本类型时使用， 性能最优 */
    public static <T> T copy(Object source, Class<T> targetClass) {

        ConcurrentHashMap<Class<?>, BeanCopier> map = cache.get(source.getClass());
        if (map == null) {
            cache.putIfAbsent(source.getClass(), new ConcurrentHashMap<>());
            map = cache.get(source.getClass());
        }
        BeanCopier copier = map.get(targetClass);
        if (copier == null) {
            map.putIfAbsent(targetClass, BeanCopier.create(source.getClass(), targetClass, false));
            copier = map.get(targetClass);
        }
        T targetObj;
        try {
            targetObj = targetClass.newInstance();
            copier.copy(source, targetObj, new Converter() {
                @SuppressWarnings("rawtypes")
                @Override
                public Object convert(Object var1, Class var2, Object var3) {
                    return var1;
                }
            });
            return targetObj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(targetClass.getName() + " could not initial ! ", e);
        }
    }


    /**
     * 列表，浅拷贝， 参数对象没有其他复杂数据类型时使用 , 性能比deepCopy高
     * 
     * @param source ，可以是单个对象或者是列表,如果是列表则返回List
     * @param targetClass ，返回的对象类型
     * @return 空列表或者是List(类型为targetClass)
     */
    public static <T, E> List<T> copy(List<E> source, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(source))
            return Collections.emptyList();
        return source.stream().map(sourceObj -> copy(sourceObj, targetClass)).collect(Collectors.toList());

    }


    /**
     * 深拷贝,对象存在其它引用时候使用
     * 
     * @param source ，源对象
     * @param targetClass ，返回的对象类型
     * @return null 或者 targetClass
     */
    public static <T> T deepCopy(Object source, Class<T> targetClass) {
        if (source == null)
            return null;

        return JSON.parseObject(JSON.toJSONBytes(source), targetClass);
    }


    /**
     * 深拷贝,对象存在其它引用时候使用
     * 
     * @param source ，源对象列表
     * @param targetClass ，返回的对象类型
     * @return 空列表 或者是List(类型为targetClass)
     */
    public static <T, E> List<T> deepCopy(List<E> source, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(source))
            return Collections.emptyList();

        String json = JSON.toJSONString(source);
        return JSON.parseArray(json, targetClass);

    }


    /** 深克隆对象 */
    public static <T> T clone(T source) {
        return KRYO.get().copy(source);
    }


    /**
     * 将对象转换成Map
     * 
     * @param bean
     * @return map ，key为属性名，如果属性为复杂类型，则为嵌套map
     */
    public static Map<String, Object> bean2Map(Object bean) {
        Map<String, Object> map = new HashMap<>(1);
        BeanMap beanMap = BeanMap.create(bean);
        Set<?> set = beanMap.keySet();
        for (Object key : set) {
            Object value = beanMap.get(key);
            bean2MapRecursion(map, value, key.toString());
        }

        return map;
    }


    private static void bean2MapRecursion(Map<String, Object> map, Object bean, String propertyName) {
        if (bean != null) {
            try {
                if (isPrimitive(bean.getClass())) {
                    map.put(propertyName, bean);
                } else if (Object[].class.isInstance(bean)) {
                    List<Map<String, Object>> list = new ArrayList<>();
                    for (Object item : (Object[]) bean) {
                        list.add(bean2Map(item));
                    }
                    map.put(propertyName, list);
                } else if (Collection.class.isInstance(bean)) {
                    List<Map<String, Object>> list = new ArrayList<>();
                    for (Object item : (Collection<?>) bean) {
                        list.add(bean2Map(item));
                    }
                    map.put(propertyName, list);
                } else if (Map.class.isInstance(bean)) {
                    Map<String, Object> subMap = new HashMap<>();
                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) bean).entrySet()) {
                        bean2MapRecursion(subMap, entry.getValue(), entry.getKey().toString());
                    }
                    map.put(propertyName, subMap);
                } else {
                    Map<String, Object> subMap = new HashMap<>();
                    BeanMap beanMap = BeanMap.create(bean);
                    Set<?> set = beanMap.keySet();
                    for (Object key : set) {
                        Object value = beanMap.get(key);
                        bean2MapRecursion(subMap, value, key.toString());
                    }
                    map.put(propertyName, subMap);
                }
            } catch (RuntimeException e) {
                log.error("propertyName = {} , value = ", propertyName, bean);
                throw e;
            }
        }
    }


    /**
     * 将对象转换成json字符串
     * 
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, SERIALIZER_FEATURES);
    }


    /**
     * json文本转换成java对象
     * 
     * @param jsonText
     * @param clazz 对象类型
     * @return
     */
    public static <T> T parseObject(String jsonText, Class<T> clazz) {
        return JSON.parseObject(jsonText, clazz, FASTJSON_CONFIG.getFeatures());
    }


    /**
     * json文本转换成List java对象
     * 
     * @param jsonText
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArray(String jsonText, Class<T> clazz) {
        return JSON.parseArray(jsonText, clazz);
    }


    /**
     * 是否基本类型
     * 
     * @param cls
     * @return
     */
    public static boolean isPrimitive(Class<?> cls) {
        if (cls.isArray()) {
            return isPrimitive(cls.getComponentType());
        }

        return cls.isPrimitive() || cls == Class.class || cls == String.class || cls == Boolean.class
                || cls == Character.class || Number.class.isAssignableFrom(cls)
                || Date.class.isAssignableFrom(cls);
    }

}
