package com.jupiterframework.servicegovernance.monitor.support;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jupiterframework.servicegovernance.monitor.domain.StatisticsDimension;
import com.jupiterframework.servicegovernance.monitor.domain.StatisticsInstance;


public class StatisticsMetric {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsMetric.class);

    private static final ConcurrentMap<StatisticsDimension, AtomicReference<StatisticsInstance>> statisticsMap = new ConcurrentHashMap<>();


    private StatisticsMetric() {
    }


    /**
     * 服务每一次执行都将结果在内存里累加
     * 
     * @param parameters
     */
    public static void collect(StatisticsDimension dimension, StatisticsInstance item) {

        // 初始化原子引用
        AtomicReference<StatisticsInstance> reference = statisticsMap.get(dimension);
        if (reference == null) {
            if (statisticsMap.putIfAbsent(dimension, new AtomicReference<>(item)) == null) {
                return;
            } else {
                reference = statisticsMap.get(dimension);
            }
        }

        // CompareAndSet并发加入统计数据
        StatisticsInstance current;
        StatisticsInstance update;
        do {
            current = reference.get();
            update = item.merge(current);
        } while (!reference.compareAndSet(current, update));

    }


    /**
     * 返回内存中统计的数据,并将内存清零
     * 
     * @return
     */
    public static synchronized Map<String/* 维度 ：取值为服务代码/渠道、 */, Map<String/*
                                                                           * 维度具体值
                                                                           */, StatisticsInstance>> getStatistics() {

        Map<String, Map<String, StatisticsInstance>> dimensionMonitorData = new HashMap<>();

        // Statistics声明的属性就是监控维度
        for (Field field : StatisticsDimension.class.getDeclaredFields()) {
            dimensionMonitorData.put(field.getName(), new HashMap<String, StatisticsInstance>());
        }

        Set<Entry<StatisticsDimension, AtomicReference<StatisticsInstance>>> currentStatisticsMap = new HashSet<>();
        currentStatisticsMap.addAll(statisticsMap.entrySet());// 如果直接操作statisticsMap，若此时添加了新的值，会有并发问题

        for (Entry<StatisticsDimension, AtomicReference<StatisticsInstance>> entry : currentStatisticsMap) {
            // 获取已统计数据，
            AtomicReference<StatisticsInstance> reference = entry.getValue();

            StatisticsInstance currentStatisticsItem; // 当前的统计数据
            do {
                currentStatisticsItem = reference.get();
            } while (!reference.compareAndSet(currentStatisticsItem, null));// 将statisticsMap的值置空

            if (currentStatisticsItem == null) {
                continue;
            }

            StatisticsDimension statisticsDimension = entry.getKey();

            // 迭代维度 ， 从不同维度统计
            for (Entry<String, Map<String, StatisticsInstance>> dimensionConcurrent : dimensionMonitorData.entrySet()) {

                String demensionKey = dimensionConcurrent.getKey();// 维度
                String dimensionValue = null; // 维度具体值
                try {
                    dimensionValue = String.valueOf(org.apache.commons.lang3.reflect.FieldUtils.readDeclaredField(statisticsDimension, demensionKey, true));
                } catch (Throwable e) {
                    LOGGER.error("", e);
                }

                if (dimensionValue == null) {
                    continue;
                }

                Map<String, StatisticsInstance> map = dimensionConcurrent.getValue();// 维度的所有具体数值
                StatisticsInstance statisticsItem = map.get(dimensionValue);

                // 合并数据
                map.put(dimensionValue, statisticsItem == null ? currentStatisticsItem : statisticsItem.merge(currentStatisticsItem));
            }
        }

        return dimensionMonitorData;
    }

}