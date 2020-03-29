package com.jupiterframework.channel.resolver;

import java.util.Map;


/**
 * 值转换器
 */
public interface ValueResolver {
    /**
     * 
     * @param params 请求或者响应的参数
     * @param value
     * @return
     */
    String resolve(Map<String, Object> params, String value);
}
