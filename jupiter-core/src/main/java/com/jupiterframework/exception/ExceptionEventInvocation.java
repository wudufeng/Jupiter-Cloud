package com.jupiterframework.exception;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 注解@async异步调用时抛出异常，触发对应的事件处理
 * 
 * @author wudf
 *
 */
@Slf4j
@AllArgsConstructor
public final class ExceptionEventInvocation {
    private Collection<ExceptionEvent> uncaughtExceptionHandlers;


    public void invoke(Throwable e) {
        for (ExceptionEvent handler : uncaughtExceptionHandlers) {
            try {
                handler.doEvent(e);
            } catch (Exception e2) {
                log.warn("", e2);
            }
        }
    }

}
