package com.jupiterframework.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jupiterframework.web.interceptor.FeignClientInterceptor;


@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.TYPE })
@Documented
@EnableFeignClients
@EnableCircuitBreaker
@EnableDiscoveryClient
@Configuration

@Import(FeignClientInterceptor.class)
public @interface SpiClients {

    String[] basePackages() default {};
}
