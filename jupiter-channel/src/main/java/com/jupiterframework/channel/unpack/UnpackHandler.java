package com.jupiterframework.channel.unpack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jupiterframework.channel.config.Response;
import com.jupiterframework.channel.config.Response.Field;
import com.jupiterframework.channel.resolver.ValueResolverFactory;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class UnpackHandler<T> {
    @Autowired
    private ValueResolverFactory valueResolverFactory;


    public Map<String, Object> handle(byte[] respData, Response response) {
        T ctx = this.parseObj(respData);

        List<Field> field = response.getFields();
        Map<String, Object> result = new HashMap<>(field.size());
        if (response.isPayload()) {
            result.put(Response.PAYLOAD_KEY, JSON.parseObject(respData, String.class));
        }

        for (Field f : field) {
            this.transform(ctx, null, f, result);
        }

        return result;
    }


    protected void transform(T obj, String parentPath, Field f, Map<String, Object> result) {
        String path = getPath(parentPath, f);

        if (List.class.isAssignableFrom(f.getType())) {
            handleList(obj, f, result, path);

        } else if (Map.class.isAssignableFrom(f.getType())) {
            handleMap(obj, f, result, path);
        } else {
            this.convertValue(readPathValue(obj, path), f, result);
        }
    }


    protected abstract T parseObj(byte[] respData);


    /** 转换、拼接要解析的path */
    protected abstract String getPath(String parentPath, Field f);


    /** 从对象中获取path的值 */
    protected abstract String readPathValue(T obj, String path);


    protected abstract void handleList(T obj, Field f, Map<String, Object> result, String path);


    protected abstract void handleMap(T obj, Field f, Map<String, Object> result, String path);


    private final void convertValue(String val, Field f, Map<String, Object> result) {
        if (!StringUtils.hasText(val)) {
            if (f.getDefaultValue() != null) {
                val = f.getDefaultValue();
            } else {
                result.put(f.getName(), null);
                return;
            }
        }

        // 数据字典映射转换
        if (f.getValueMappings() != null && f.getValueMappings().containsKey(val)) {
            val = f.getValueMappings().get(val);
        }

        // 值解析/加、解密
        if (StringUtils.hasText(f.getResolver())) {
            val = valueResolverFactory.create(f.getResolver()).resolve(null, val);
        }

        switch (f.getType().getName()) {
        case "java.lang.String":
            result.put(f.getName(), val);
            return;
        case "java.math.BigDecimal":
            result.put(f.getName(), new BigDecimal(val));
            return;
        case "java.math.BigInteger":
            result.put(f.getName(), new BigInteger(val));
            return;
        case "java.lang.Boolean":
            result.put(f.getName(), Boolean.valueOf(val));
            return;
        case "java.lang.Byte":
            result.put(f.getName(), Byte.valueOf(val));
            return;
        case "java.util.Date":
            // 取当前时间
            if (f.getDateFormat().equals("currentTime")) {
                result.put(f.getName(), new Date());
                return;
            }
            if (f.getDateFormat().startsWith("###")) {
                /* 时间戳格式 */
                result.put(f.getName(), new Date(Long.valueOf(f.getDateFormat().replace("###", val))));
                return;
            }
            try {
                result.put(f.getName(), DateUtils.parseDate(val, f.getDateFormat()));
            } catch (ParseException e) {
                throw new IllegalArgumentException(val + " 无法转换为日期格式" + f.getDateFormat());
            }

            return;
        case "java.lang.Double":
            result.put(f.getName(), Double.valueOf(val));
            return;
        case "java.lang.Float":
            result.put(f.getName(), Float.valueOf(val));
            return;
        case "java.lang.Integer":
            result.put(f.getName(), Integer.valueOf(val));
            return;

        case "java.lang.Long":
            result.put(f.getName(), Long.valueOf(val));
            return;
        default:
            log.warn("值{}匹配不到合适的类型{}", val, f.getType());
            result.put(f.getName(), val);
        }
    }

}
