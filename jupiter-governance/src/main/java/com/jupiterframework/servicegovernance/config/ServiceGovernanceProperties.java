package com.jupiterframework.servicegovernance.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;


@ConfigurationProperties(prefix = "service.governance")
@Data
public class ServiceGovernanceProperties {

    private int statisticsPeriod = 60;

    private Elapsed elapsed = new Elapsed();

    /** 要忽略的异常码 */
    private List<String> ignoreErrorCode = new ArrayList<>(0);

    @Data
    public static class Elapsed {
        private long warn = 5000L * 1000000L;// 超过5秒耗时打印warn级别
        private long info = 1000L * 1000000L;// 超过1000毫秒耗时打印info级别
    }
}
