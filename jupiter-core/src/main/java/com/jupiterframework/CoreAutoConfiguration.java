package com.jupiterframework;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jupiterframework.exception.ExceptionEvent;
import com.jupiterframework.exception.ExceptionEventInvocation;


@Configuration
public class CoreAutoConfiguration {

	@Bean
	public ExceptionEventInvocation uncaughtExceptionHandlerInvocation(ApplicationContext beanFactory) {

		return new ExceptionEventInvocation(beanFactory.getBeansOfType(ExceptionEvent.class).values());
	}

}
