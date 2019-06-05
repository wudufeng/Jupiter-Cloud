package com.jupiterframework.servicegovernance.monitor;

import com.jupiterframework.servicegovernance.monitor.domain.StatisticsDimension;
import com.jupiterframework.servicegovernance.monitor.domain.StatisticsInstance;


/**
 * 统计
 * 
 * @author wudf
 *
 */
public interface StatisticsService {

    /**
     * 收集统计信息
     * 
     * @param dimension
     * @param item
     */
    void collect(StatisticsDimension dimension, StatisticsInstance item);


    /**
     * 并发数+1,在服务入口调用
     * 
     * @param serviceCode
     * @return
     */
    int incrementAndGetConcurrent(String serviceCode);


    /**
     * 并发数-1，服务接口结束后调用
     * 
     * @param serviceCode
     */
    void decrementConcurrent(String serviceCode);

}
