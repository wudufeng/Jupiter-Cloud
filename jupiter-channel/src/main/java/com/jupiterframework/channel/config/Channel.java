package com.jupiterframework.channel.config;

import java.util.HashMap;
import java.util.Map;


/**
 * 单个渠道（第三方系统）下的通用配置
 */
@lombok.Data
public class Channel {
    private String name;
    private RequestMethod requestMethod = RequestMethod.HTTP_POST;
    private String url = "";
    private int connectTimeout = 10000;
    private int socketTimeout = 30000;

    private Authorized authorized;

    private Map<String /* identified */, Service> services = new HashMap<>();
}
