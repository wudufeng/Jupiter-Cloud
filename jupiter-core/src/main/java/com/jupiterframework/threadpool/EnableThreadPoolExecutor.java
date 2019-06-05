package com.jupiterframework.threadpool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;


/**
 * 添加此注解，使用自定义线程池
 * 
 * @author wudf
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ThreadPoolAutoConfiguration.class)
public @interface EnableThreadPoolExecutor {

}
