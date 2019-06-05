package com.jupiterframework.threadpool.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;


@Data
@ConfigurationProperties(prefix = "threadpool")
public class ThreadPoolProperties {
    private int corePoolSize = 20;
    private int maxPoolSize = 100;
    private int queueCapacity = 0;
    private int keepAliveSeconds = 120;

    private String threadNamePrefix = "UserTask-";

}
