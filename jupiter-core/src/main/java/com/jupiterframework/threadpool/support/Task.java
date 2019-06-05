package com.jupiterframework.threadpool.support;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * 任务处理类
 */
@Data
@AllArgsConstructor
public abstract class Task<T, V> {
    /** 是否需要开启事务处理 */
    private boolean beginTransaction = false;


    public abstract V execute(T param) throws Exception;

}
