package com.ueb.framework.channel.resolver;

import java.util.Map;


/**
 * 值转换器
 */
public interface ValueResolver {

    String resolve(Map<String, String> queryParams, String value);
}
