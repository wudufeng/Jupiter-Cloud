package com.jupiterframework.servicegovernance.monitor.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * 监控统计数据 项
 *
 */
@Getter
@EqualsAndHashCode
public class StatisticsInstance {
    private final long success;
    private final long failure;
    private final long elapsed;// 耗时(毫秒)
    private final int concurrent;// 当前并发数

    private int maxConcurrent;
    private long maxElapsed;
    private long minElapsed;


    /**
     * 
     * @param success 是否成功
     * @param elapsed 耗时(纳秒) System.nanoTime()
     * @param concurrent 当前记录的并发数(计数器取开始前+1的值, 接口处理完毕后-1)
     */
    public StatisticsInstance(boolean success, long elapsed, int concurrent) {
        this(success ? 1 : 0, success ? 0 : 1, elapsed, concurrent);
    }


    private StatisticsInstance(long success, long failure, long elapsed, int concurrent) {
        super();
        this.success = success;
        this.failure = failure;
        this.elapsed = elapsed;
        this.concurrent = concurrent;

        this.maxElapsed = elapsed;
        this.maxConcurrent = concurrent;
        this.minElapsed = elapsed;
    }


    /**
     * 累加
     * 
     * @param statisticsItem
     * @return
     */
    public StatisticsInstance merge(StatisticsInstance statisticsItem) {
        if (statisticsItem == null)
            return this;
        StatisticsInstance update = new StatisticsInstance(this.success + statisticsItem.success, this.failure + statisticsItem.failure,
            (this.elapsed + statisticsItem.elapsed) / 2, (this.concurrent + statisticsItem.concurrent) / 2);

        update.maxElapsed = this.maxElapsed > statisticsItem.maxElapsed ? this.maxElapsed : statisticsItem.maxElapsed;
        update.maxConcurrent = this.maxConcurrent > statisticsItem.maxConcurrent ? this.maxConcurrent : statisticsItem.maxConcurrent;
        update.minElapsed = this.minElapsed < statisticsItem.minElapsed ? this.minElapsed : statisticsItem.minElapsed;

        return update;
    }


    /**
     * 成功次数|失败次数|平均并发次数|最大并发次数|平均耗时|最大耗时|最小耗时
     */
    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s|%s", success, failure, concurrent, maxConcurrent, elapsed, maxElapsed, minElapsed);
    }
}
