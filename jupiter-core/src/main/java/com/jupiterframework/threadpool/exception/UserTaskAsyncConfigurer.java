package com.jupiterframework.threadpool.exception;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import com.jupiterframework.exception.ExceptionEventInvocation;
import com.jupiterframework.threadpool.support.ThreadPoolExecutor;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 使用@Async注解指定线程池，以及异常处理类
 * 
 * @author wudf
 *
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskAsyncConfigurer implements AsyncConfigurer {

    private ThreadPoolExecutor threadPoolExecutor;
    private ExceptionEventInvocation handler;


    @Override
    public Executor getAsyncExecutor() {
        return threadPoolExecutor;
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                if (log.isErrorEnabled()) {
                    log.error("Unexpected error occurred invoking async method '{}'.", method, ex);
                }
                handler.invoke(ex);
            }
        };
    }

}
