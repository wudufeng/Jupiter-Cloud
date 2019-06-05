package com.jupiterframework.transaction;

/**
 * 判断事务是否存在当前线程
 * 
 * @author wudf
 *
 */
public interface TransactionDetermine {

    boolean isActualTransactionActive();
}
