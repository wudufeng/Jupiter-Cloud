package com.jupiterframework.threadpool.support;

import lombok.Data;


/**
 * 任务处理结果
 * 
 * @author wudf
 *
 * @param <E> 原数据
 * @param <V> 返回值
 */
@Data
public class TaskResult<E, V> {

    /** 原数据 */
    private E originData;

    /**
     * TaskCallable的返回值
     */
    private V value;

    /** 执行TaskCallable的异常 */
    private Exception exception;


    public boolean hashException() {
        return exception != null;
    }

}
