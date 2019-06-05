package com.jupiterframework.transaction;

/**
 * 如果classpath里存在事务管理器，则使用springframework.TransactionSynchronizationManager判断当前线程是否在事务里
 * 
 * @author wudf
 *
 */
public class TransactionDetermineManager implements TransactionDetermine {

    @Override
    public boolean isActualTransactionActive() {
        return org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive();
    }

}
