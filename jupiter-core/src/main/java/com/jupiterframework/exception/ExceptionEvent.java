package com.jupiterframework.exception;

/**
 * 异常事件接口<br/>
 * 注解@async异步调用时抛出异常，触发事件处理接口,异常堆栈已统一打印，不要在接口实现里再打印异常堆栈<br/>
 * 
 * 不需要往外抛出的异常，需要注入此注解
 * 
 * @author wudf
 *
 */
public interface ExceptionEvent {

    void doEvent(Throwable e);
}
