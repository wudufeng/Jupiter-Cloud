package com.jupiterframework.transaction;

import org.springframework.core.annotation.Order;


@Order(value = Integer.MAX_VALUE)
public interface TransactionActionListener {
    /** 事务提交前触发 ,此方法的异常会往上抛 */
    void doBeforeCommit();


    /** 事务提交成功后触发 ,如果方法抛出异常，会在事务管理器catch掉 */
    void doAfterCommit();


    /** 事务回滚成功后触发 ,如果方法抛出异常，会在事务管理器catch掉 */
    void doBeforeRollback();


    /** 事务提交或者回滚都会触发,如果方法抛出异常，会在事务管理器catch掉 */
    void doCleanupAfterCompletion();
}
