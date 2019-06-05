package com.jupiterframework.servicegovernance.monitor.support;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.jupiterframework.servicegovernance.constant.ServiceGovernanceConstants;
import com.jupiterframework.servicegovernance.monitor.domain.StatisticsInstance;

import lombok.extern.slf4j.Slf4j;


/**
 * 每一分钟将内存统计的数据输出到日志文件
 * 
 * @author wudf
 *
 */
@Slf4j
public class StatisticsMonitorTrigger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceGovernanceConstants.MONITOR_LOGGER_NAME);
    private static final String MDC_DIMENSION = "_dimension";
    private static final String MDC_DIMENSION_VALUE = "_dimension_val";

    private int period = 60;

    private static final Field[] fields = StatisticsInstance.class.getDeclaredFields();
    private final ScheduledExecutorService scheduledExecutorService;


    public StatisticsMonitorTrigger(int period) {
        if (period > 5 && period <= 60) {
            log.info("set statistics monitor period {}", period);
            this.period = period;
        }

        scheduledExecutorService = new ScheduledThreadPoolExecutor(2, new NamedThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
    }

    private static class NamedThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;


        private NamedThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "statistics-monitor-trigger-" + poolNumber.getAndIncrement() + "-thread-";
        }


        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }


    @PostConstruct
    public void start() {
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {

            @Override
            public void run() {
                try {
                    doCollect();
                } catch (Throwable e) {
                    log.warn("采集服务监控统计数据异常", e);
                }

            }
        }, 10, period, TimeUnit.SECONDS);
    }


    @PreDestroy
    public void destroy() {
        scheduledExecutorService.shutdown();
    }


    private void doCollect() {
        for (Entry<String, Map<String, StatisticsInstance>> entry : StatisticsMetric.getStatistics().entrySet()) {
            MDC.put(MDC_DIMENSION, entry.getKey());

            if (entry.getValue() != null) {
                for (Entry<String, StatisticsInstance> item : entry.getValue().entrySet()) {

                    StatisticsInstance val = item.getValue();
                    MDC.put(MDC_DIMENSION_VALUE, item.getKey());

                    if (val != null) {
                        try {
                            for (Field f : fields) {
                                MDC.put("_inst." + f.getName(), String.valueOf(org.apache.commons.lang3.reflect.FieldUtils.readField(f, val, true)));
                            }
                            LOGGER.trace("");
                        } catch (Exception e) {
                            log.warn("", e);
                        } finally {
                            for (Field f : fields)
                                MDC.remove("_inst." + f.getName());
                        }

                    }

                    MDC.remove(MDC_DIMENSION_VALUE);
                }
            }
            MDC.remove(MDC_DIMENSION);
        }

    }
}
