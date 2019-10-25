package com.jupiterframework.channel.config;

import java.util.HashMap;
import java.util.Map;


@lombok.Data
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "channel")
public class ChannelProperties<T extends Channel> {
    /** 指定配置文件的目录 */
    private String path = "classpath*:channel";

    private Map<String /* name */, T> channelConfigurations = new HashMap<>();


    public T getChannelConfiguration(String name) {
        return channelConfigurations.get(name);
    }

}
