package com.jupiterframework.transaction;

/**
 * 当前应该无开启事务
 * 
 * @author wudf
 *
 */
public class DisableTransactionDetermine implements TransactionDetermine {

    @Override
    public boolean isActualTransactionActive() {
        return false;
    }

}
