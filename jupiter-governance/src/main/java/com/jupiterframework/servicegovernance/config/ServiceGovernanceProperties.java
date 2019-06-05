package com.jupiterframework.servicegovernance.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;


@ConfigurationProperties(prefix = "service.governance")
@Data
public class ServiceGovernanceProperties {

    private int statisticsPeriod = 60;

    private long warnElapsed = 3000L * 1000000L;// 超过3秒耗时打印warn级别

    private long infoElapsed = 500L * 1000000L;// 超过500毫秒耗时打印info级别

    /** 要忽略的异常码 */
    private List<String> ignoreErrorCode = new ArrayList<>(0);
}
