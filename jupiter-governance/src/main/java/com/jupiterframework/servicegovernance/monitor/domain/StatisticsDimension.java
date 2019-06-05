package com.jupiterframework.servicegovernance.monitor.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


/**
 * 统计维度
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class StatisticsDimension {

    /** 监控的数据项,默认是uri */
    private final String serviceCode;

}