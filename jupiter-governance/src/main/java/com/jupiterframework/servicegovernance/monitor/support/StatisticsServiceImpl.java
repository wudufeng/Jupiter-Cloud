package com.jupiterframework.servicegovernance.monitor.support;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.jupiterframework.servicegovernance.monitor.StatisticsService;
import com.jupiterframework.servicegovernance.monitor.domain.StatisticsDimension;
import com.jupiterframework.servicegovernance.monitor.domain.StatisticsInstance;


public class StatisticsServiceImpl implements StatisticsService {
    /** 并发计数器 */
    private static final ConcurrentMap<String, AtomicInteger> concurrents = new ConcurrentHashMap<>();


    @Override
    public void collect(StatisticsDimension dimension, StatisticsInstance item) {
        StatisticsMetric.collect(dimension, item);
    }


    @Override
    public int incrementAndGetConcurrent(String serviceCode) {

        return getConcurrent(serviceCode).incrementAndGet();
    }


    @Override
    public void decrementConcurrent(String serviceCode) {
        getConcurrent(serviceCode).decrementAndGet();
    }


    // 获取并发计数器
    private AtomicInteger getConcurrent(String key) {

        AtomicInteger concurrent = concurrents.get(key);
        if (concurrent == null) {
            concurrents.putIfAbsent(key, new AtomicInteger());
            concurrent = concurrents.get(key);
        }

        return concurrent;
    }

}