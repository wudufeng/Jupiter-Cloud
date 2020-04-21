package com.jupiterframework.channel.unpack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.mvel2.MVEL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableMap;
import com.jupiterframework.channel.config.Response;
import com.jupiterframework.channel.config.Response.Field;
import com.jupiterframework.channel.resolver.ValueResolverFactory;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class UnpackHandler<T> {
    @Autowired
    private ValueResolverFactory valueResolverFactory;

    private static final ThreadLocal<Map<String/* mvel */, List<Map<Field/*
                                                                          * Field
                                                                          */, Map<String, Object> /*
                                                                                                   * 计算Field值时候当前的resultMap
                                                                                                   */>>>> MVELS = new ThreadLocal<>();

    protected static final String LIST_ITEM_INDEX_KEY_STRING = "_index_";

    public Map<String, Object> handle(String respData, Response response) {
        return this.handle(respData.getBytes(StandardCharsets.UTF_8), response, null);
    }


    public Map<String, Object> handle(String respData, Response response, Map<String, Object> paramMap) {
        return this.handle(respData.getBytes(StandardCharsets.UTF_8), response, paramMap);
    }


    public Map<String, Object> handle(byte[] respData, Response response) {
        return this.handle(respData, response, null);
    }


    public Map<String, Object> handle(byte[] respData, Response response, Map<String, Object> paramMap) {
        T ctx = this.parseObj(respData);
        String indicateVal = null;
        if (StringUtils.hasText(response.getIndicate())) {
            indicateVal = this.readPathValue(ctx, response.getIndicate(), String.class);
            if (indicateVal != null) {
                log.debug("receive response : {}", indicateVal);
                ctx = this.parseObj(indicateVal.getBytes(StandardCharsets.UTF_8));
            }
        }

        List<Field> field = response.getFields();
        Map<String, Object> result = paramMap == null ? new HashMap<>(field.size()) : new HashMap<>(paramMap);// 响应数据会写入到此map
                                                                                                              // 重新创建一个用于防止脏数据写到此处
        if (org.apache.commons.lang3.StringUtils.isNotBlank(response.getPayload())) {
            result.put(Response.PAYLOAD_KEY, this.readPathValue(ctx, response.getPayload(), Properties.class));
        }

        try {
            for (Field f : field) {
                this.transform(ctx, null, f, result);
            }

            // 执行mvel表达式，并重设置属性值
            if (MVELS.get() != null) {
                for (Entry<String, List<Map<Field, Map<String, Object>>>> x : MVELS.get().entrySet()) {
                    Object val = null;
                    try {
                        val = MVEL.eval(x.getKey(), result);
                        for (Map<Field, Map<String, Object>> a : x.getValue()) {
                            Entry<Field, Map<String, Object>> entry = a.entrySet().iterator().next();
                            entry.getValue().put(entry.getKey().getName(),
                                (val == null || entry.getKey().getType().isAssignableFrom(val.getClass())) ? val : this.parseValue(entry.getKey(), String.valueOf(val)));
                        }
                    } catch (Exception e) {
                        log.error("解析mvel[{}] -> [{}], 错误 {}", x.getKey(), val, ExceptionUtils.getRootCauseMessage(e));
                        log.error("已完成解析数据{}", result);
                        throw e;
                    }
                }
            }
        } finally {
            MVELS.set(null);
        }

        return result;
    }


    protected void transform(T obj, String parentPath, Field f, Map<String, Object> result) {
        String path = getPath(parentPath, f);
        if (path.indexOf(LIST_ITEM_INDEX_KEY_STRING) > -1)
            path = path.replace(LIST_ITEM_INDEX_KEY_STRING, String.valueOf(result.get(LIST_ITEM_INDEX_KEY_STRING)));

        if (List.class.isAssignableFrom(f.getType())) {
            handleList(obj, f, result, path);

        } else if (Map.class.isAssignableFrom(f.getType())) {
            handleMap(obj, f, result, path);
        } else {
            this.convertValue(readPathValue(obj, path, String.class), f, result);

            if (org.apache.commons.lang3.StringUtils.isNotBlank(f.getMvelExpression())) {
                if (MVELS.get() == null)
                    MVELS.set(new LinkedHashMap<>());
                Map<Field, Map<String, Object>> pairs = ImmutableMap.of(f, result);
                MVELS.get()
                    .computeIfAbsent(f.getMvelExpression().indexOf(LIST_ITEM_INDEX_KEY_STRING) > -1
                            ? f.getMvelExpression().replace(LIST_ITEM_INDEX_KEY_STRING, String.valueOf(result.get(LIST_ITEM_INDEX_KEY_STRING)))
                            : f.getMvelExpression(),
                        x -> new ArrayList<>())
                    .add(pairs);
            }
        }
    }


    protected abstract T parseObj(byte[] respData);


    /** 转换、拼接要解析的path */
    protected abstract String getPath(String parentPath, Field f);


    /** 从对象中获取path的值 */
    protected abstract <E> String readPathValue(T obj, String path, Class<E> clazz);


    protected abstract void handleList(T obj, Field f, Map<String, Object> result, String path);


    protected abstract void handleMap(T obj, Field f, Map<String, Object> result, String path);


    private final void convertValue(String val, Field f, Map<String, Object> result) {
        if (!StringUtils.hasText(val)) {
            if (f.getDefaultValue() != null) {
                val = f.getDefaultValue();
            } else if (!result.containsKey(f.getName())) {
                result.put(f.getName(), val);
                return;
            } else {
                return;
            }
        } else if (f.getValueMappings() != null) {
            // 数据字典映射转换
            if (f.getValueMappings().containsKey(val)) {
                val = f.getValueMappings().get(val);
            } else if (f.getValueMappings().containsKey("*")) {
                val = f.getValueMappings().get("*");
            }
        }

        // 值解析/加、解密
        if (StringUtils.hasText(f.getResolver())) {
            for (String resolver : f.getResolver().split(","))
                val = valueResolverFactory.create(resolver).resolve(result, val);
        }

        try {
            result.put(f.getName(), this.parseValue(f, val));
        } catch (RuntimeException e) {
            log.error("解析{}失败，值{}", f.getName(), val);
            throw e;
        }
    }


    private Object parseValue(Field f, String val) {
        if (val == null)
            return null;
        switch (f.getType().getName()) {
        case "java.lang.String":
            return val;
        case "java.math.BigDecimal":
            return new BigDecimal(val);
        case "java.math.BigInteger":
            return new BigInteger(val);
        case "java.lang.Boolean":
            return Boolean.valueOf(val);
        case "java.lang.Byte":
            return Byte.valueOf(val);
        case "java.util.Date":
            // 取当前时间
            if (val.equals("currentTime")) {
                return new Date();
            }
            Date date = null;
            if (f.getDateFormat().startsWith("###")) {
                /* 时间戳格式 */
                date = new Date(Long.valueOf(f.getDateFormat().trim().replace("###", val)));
            } else {
                try {
                    date = DateUtils.parseDate(val, f.getDateFormat().trim().trim());
                } catch (ParseException e) {
                    throw new IllegalArgumentException(val + " 无法转换为日期格式" + f.getDateFormat());
                }
            }
            if (f.getAmountHours() != null) {
                date = DateUtils.addHours(date, f.getAmountHours());
            }

            return date;
        case "java.lang.Double":
            return Double.valueOf(val);
        case "java.lang.Float":
            return Float.valueOf(val);
        case "java.lang.Integer":
            return Integer.valueOf(val);

        case "java.lang.Long":
            return Long.valueOf(val);
        default:
            log.warn("值{}匹配不到合适的类型{}", val, f.getType());
            return val;
        }
    }
}
