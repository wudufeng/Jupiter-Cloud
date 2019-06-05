package com.jupiterframework.threadpool.support;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.jupiterframework.threadpool.config.ThreadPoolProperties;


public class ThreadPoolFactory {
	private ThreadPoolFactory() {
	}

	private static final AtomicInteger POOL_COUNTER = new AtomicInteger(0);

	public static ThreadPoolExecutor createThreadPoolExecutor(ApplicationContext beanFactory) throws Exception {
		ThreadPoolProperties prop = beanFactory.getBean(ThreadPoolProperties.class);
		return createThreadPoolExecutor(beanFactory, prop.getCorePoolSize(), prop.getMaxPoolSize(),
			prop.getQueueCapacity(), prop.getKeepAliveSeconds(),
			prop.getThreadNamePrefix() + POOL_COUNTER.getAndIncrement() + "-");
	}

	public static ThreadPoolExecutor createThreadPoolExecutor(ApplicationContext applicationContext, int corePoolSize,
			int maxPoolSize, int queueCapacity, int keepAliveSeconds, String threadNamePrefix) throws Exception {

		ThreadPoolTaskExecutor delegate = new ThreadPoolTaskExecutor();

		ThreadPoolExecutor executor;
		try {
			// 避免没有引用 sleuth
			ThreadPoolTaskExecutor tt = (ThreadPoolTaskExecutor) Class
				.forName("org.springframework.cloud.sleuth.instrument.async.LazyTraceThreadPoolTaskExecutor")
				.getConstructor(BeanFactory.class, ThreadPoolTaskExecutor.class)
				.newInstance(applicationContext, delegate);
			executor = new com.jupiterframework.threadpool.support.ThreadPoolExecutor(tt);
		} catch (ClassNotFoundException e) {
			executor = new com.jupiterframework.threadpool.support.ThreadPoolExecutor(delegate);
		}

		executor.setApplicationContext(applicationContext);
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}
