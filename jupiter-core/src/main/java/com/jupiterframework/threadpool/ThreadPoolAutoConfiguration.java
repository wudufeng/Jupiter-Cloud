package com.jupiterframework.threadpool;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jupiterframework.exception.ExceptionEventInvocation;
import com.jupiterframework.threadpool.config.ThreadPoolProperties;
import com.jupiterframework.threadpool.exception.UserTaskAsyncConfigurer;
import com.jupiterframework.threadpool.support.ThreadPoolExecutor;
import com.jupiterframework.threadpool.support.ThreadPoolFactory;

import lombok.Setter;


@Setter
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class ThreadPoolAutoConfiguration {

	@ConditionalOnMissingBean
	@Bean
	public ThreadPoolExecutor userTaskExecutor(ApplicationContext beanFactory) throws Exception {
		return ThreadPoolFactory.createThreadPoolExecutor(beanFactory);
	}

	@Bean
	@ConditionalOnMissingBean
	public org.springframework.scheduling.annotation.AsyncConfigurer asyncConfigurer(
			ThreadPoolExecutor threadPoolExecutor, ExceptionEventInvocation handler) {

		return new UserTaskAsyncConfigurer(threadPoolExecutor, handler);
	}

}
