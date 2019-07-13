package com.jupiterframework.channel.unpack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.jupiterframework.channel.config.Response;
import com.jupiterframework.channel.config.Response.Field;
import com.jupiterframework.channel.resolver.ValueResolverFactory;


public abstract class UnpackHandler {
    @Autowired
    private ValueResolverFactory valueResolverFactory;


    public abstract Map<String, Object> handle(byte[] respData, Response response);


    public void convertValue(String val, Field f, Map<String, Object> result) {
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
            if (f.getDateFormat().equals("currentTime"))
                result.put(f.getName(), new Date());
            else {
                try {
                    result.put(f.getName(),
                        f.getDateFormat().startsWith("###") ? new Date(Long.valueOf(f.getDateFormat().replace("###", val))) : DateUtils.parseDate(val, f.getDateFormat()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(val + " 无法转换为日期格式" + f.getDateFormat());
                }
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
            // log.warn("值{}匹配不到合适的类型{}", val, f.getType());
            result.put(f.getName(), val);
        }
    }
}
