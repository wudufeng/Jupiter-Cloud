package com.jupiterframework.threadpool.propagation;

import org.springframework.context.ApplicationContext;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 并发处理集合数据时,此抽象类封装Callable接口，用于处理参数数据传递,创建的此对象必须是多态的
 * 
 * @author wudf
 *
 * @param <T> 需要处理的参数
 * @param <V> 处理返回的参数，如无需返回，设置为Void
 */
@Slf4j
@AllArgsConstructor
// @org.apache.skywalking.apm.toolkit.trace.TraceCrossThread
public abstract class TaskCallable<T, V> implements java.util.concurrent.Callable<V> {
	private T taskData;
	private boolean beginTransaction = false;

	private ApplicationContext applicatContext;

	@Override
	public V call() throws Exception {

		// 需要开启事务处理
		if (beginTransaction) {
			org.springframework.transaction.PlatformTransactionManager txManager =
					this.getApplicatContext().getBean(org.springframework.transaction.PlatformTransactionManager.class);

			org.springframework.transaction.TransactionStatus tx = null;
			org.springframework.transaction.TransactionDefinition td =
					new org.springframework.transaction.support.DefaultTransactionDefinition(
						org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			tx = txManager.getTransaction(td);
			try {
				V value = this.execute(taskData);
				txManager.commit(tx);
				return value;
			} catch (Exception e) {
				if (!tx.isCompleted()) {
					try {
						txManager.rollback(tx);
					} catch (Exception e2) {
						log.warn(" {} , rollback error,ignore ", taskData, e2);
					}
				}
				throw e;
			}
		}

		// 无需事务处理
		return this.execute(taskData);

	}

	public abstract V execute(T parameter) throws Exception;

	protected ApplicationContext getApplicatContext() {
		return applicatContext;
	}

}
