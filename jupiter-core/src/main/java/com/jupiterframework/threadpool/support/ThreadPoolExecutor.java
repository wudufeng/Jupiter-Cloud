package com.jupiterframework.threadpool.support;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import com.jupiterframework.threadpool.propagation.TaskCallable;


/**
 * 线程池并发工具类
 * 
 * @author wudf
 *
 */
public class ThreadPoolExecutor extends ThreadPoolTaskExecutor {
	private static final long serialVersionUID = 275477927183230103L;
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private transient ApplicationContext applicationContext;

	private final ThreadPoolTaskExecutor delegate;

	public ThreadPoolExecutor(ThreadPoolTaskExecutor delegate) {
		this.delegate = delegate;
	}

	public <E, V> List<TaskResult<E, V>> submit(Collection<E> data, Task<E, V> task) {
		return this.submit(data, task, 0);
	}

	/**
	 * 并发执行指定的集合，等待所有的线程处理完毕后返回所有的执行结果
	 * 
	 * @param data
	 * @param task
	 * @param millisecondTimeout
	 * @return
	 */
	public <E, V> List<TaskResult<E, V>> submit(Collection<E> data, Task<E, V> task, long millisecondTimeout) {
		final List<E> d = new ArrayList<>(data);
		List<Future<V>> futures = new ArrayList<>(data.size());
		for (E element : d) {
			Future<V> future =
					this.submit(new TaskCallable<E, V>(element, task.isBeginTransaction(), applicationContext) {
						@Override
						public V execute(E parameter) throws Exception {
							return task.execute(parameter);
						}
					});
			futures.add(future);
		}

		List<TaskResult<E, V>> result = new ArrayList<>(data.size());
		long elapsedTime = millisecondTimeout;
		for (int i = 0, size = futures.size(); i < size; i++) {
			Future<V> f = futures.get(i);
			long startTime = System.currentTimeMillis();
			TaskResult<E, V> tr = new TaskResult<>();
			try {
				tr.setOriginData(d.get(i));
				V val = millisecondTimeout == 0 ? f.get() : f.get(elapsedTime, TimeUnit.MILLISECONDS);
				tr.setValue(val);
			} catch (TimeoutException e) {
				if ((!f.isDone()) && f.cancel(false)) {
					tr.setException(e);
					log.error("elapsed timeout {} , interrupt becuse execut [ {} ] timeout  !", millisecondTimeout,
						d.get(i), e);
				}
			} catch (InterruptedException | ExecutionException e) {
				tr.setException(e);
				log.error(" execut [ {} ] error !", d.get(i), e);
			}
			result.add(tr);

			elapsedTime -= System.currentTimeMillis() - startTime;
			if (elapsedTime <= 0) {
				elapsedTime = 1;
			}
		}
		return result;
	}

	/**
	 * 并发执行指定的集合，等待所有的线程处理完毕后返回处理失败的结果
	 * 
	 * @param data
	 * @param task
	 * @param millisecondTimeout
	 * @return 处理成功的结果，如果结果无返回值，则返回List元素为Null
	 */
	public <E, V> List<TaskResult<E, V>> execute(Collection<E> data, Task<E, V> task, long millisecondTimeout) {
		List<TaskResult<E, V>> list = this.submit(data, task, millisecondTimeout);
		List<TaskResult<E, V>> result = new ArrayList<>(list.size());
		for (TaskResult<E, V> taskResult : list) {
			if (!taskResult.hashException()) {
				result.add(taskResult);
			}
		}
		return result;
	}

	/**
	 * 并发执行指定的集合，等待所有的线程处理完毕后返回处理失败的结果
	 * 
	 * @param data
	 * @param task
	 * @param millisecondTimeout
	 * @return 处理失败的结果，如果结果无返回值，则返回List元素为Null
	 */
	public <E, V> List<TaskResult<E, V>> executeFailTaskResult(Collection<E> data, Task<E, V> task,
			long millisecondTimeout) {
		List<TaskResult<E, V>> list = this.submit(data, task, millisecondTimeout);
		List<TaskResult<E, V>> result = new ArrayList<>(list.size());
		for (TaskResult<E, V> taskResult : list) {
			if (taskResult.hashException()) {
				result.add(taskResult);
			}
		}
		return result;
	}

	//////////////////////////////////////////////////
	////// 以下代码参考 ///////////////////////////////////
	////// LazyTraceThreadPoolTaskExecutor////////////
	////// 实现装饰类SessionContinueCallable/////////////
	//////////////////////////////////////////////////

	@Override
	public void execute(Runnable task) {
		this.delegate.execute(task);
	}

	@Override
	public void execute(Runnable task, long startTimeout) {
		this.delegate.execute(task, startTimeout);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return this.delegate.submit(task);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return this.delegate.submit(task);
	}

	@Override
	public ListenableFuture<?> submitListenable(Runnable task) {
		return this.delegate.submitListenable(task);
	}

	@Override
	public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		return this.delegate.submitListenable(task);
	}

	@Override
	public boolean prefersShortLivedTasks() {
		return this.delegate.prefersShortLivedTasks();
	}

	@Override
	public void setThreadFactory(ThreadFactory threadFactory) {
		this.delegate.setThreadFactory(threadFactory);
	}

	@Override
	public void setThreadNamePrefix(String threadNamePrefix) {
		this.delegate.setThreadNamePrefix(threadNamePrefix);
	}

	@Override
	public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
		this.delegate.setRejectedExecutionHandler(rejectedExecutionHandler);
	}

	@Override
	public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
		this.delegate.setWaitForTasksToCompleteOnShutdown(waitForJobsToCompleteOnShutdown);
	}

	@Override
	public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
		this.delegate.setAwaitTerminationSeconds(awaitTerminationSeconds);
	}

	@Override
	public void setBeanName(String name) {
		this.delegate.setBeanName(name);
	}

	@Override
	public java.util.concurrent.ThreadPoolExecutor getThreadPoolExecutor() throws IllegalStateException {
		return this.delegate.getThreadPoolExecutor();
	}

	@Override
	public int getPoolSize() {
		return this.delegate.getPoolSize();
	}

	@Override
	public int getActiveCount() {
		return this.delegate.getActiveCount();
	}

	@Override
	public void destroy() {
		this.delegate.destroy();
		super.destroy();
	}

	@Override
	public void afterPropertiesSet() {
		this.delegate.afterPropertiesSet();
		super.afterPropertiesSet();
	}

	@Override
	public void initialize() {
		this.delegate.initialize();
	}

	@Override
	public void shutdown() {
		this.delegate.shutdown();
		super.shutdown();
	}

	@Override
	public Thread newThread(Runnable runnable) {
		return this.delegate.newThread(runnable);
	}

	@Override
	public String getThreadNamePrefix() {
		return this.delegate.getThreadNamePrefix();
	}

	@Override
	public void setThreadPriority(int threadPriority) {
		this.delegate.setThreadPriority(threadPriority);
	}

	@Override
	public int getThreadPriority() {
		return this.delegate.getThreadPriority();
	}

	@Override
	public void setDaemon(boolean daemon) {
		this.delegate.setDaemon(daemon);
	}

	@Override
	public boolean isDaemon() {
		return this.delegate.isDaemon();
	}

	@Override
	public void setThreadGroupName(String name) {
		this.delegate.setThreadGroupName(name);
	}

	@Override
	public void setThreadGroup(ThreadGroup threadGroup) {
		this.delegate.setThreadGroup(threadGroup);
	}

	@Override
	public ThreadGroup getThreadGroup() {
		return this.delegate.getThreadGroup();
	}

	@Override
	public Thread createThread(Runnable runnable) {
		return this.delegate.createThread(runnable);
	}

	@Override
	public void setCorePoolSize(int corePoolSize) {
		this.delegate.setCorePoolSize(corePoolSize);
	}

	@Override
	public int getCorePoolSize() {
		return this.delegate.getCorePoolSize();
	}

	@Override
	public void setMaxPoolSize(int maxPoolSize) {
		this.delegate.setMaxPoolSize(maxPoolSize);
	}

	@Override
	public int getMaxPoolSize() {
		return this.delegate.getMaxPoolSize();
	}

	@Override
	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.delegate.setKeepAliveSeconds(keepAliveSeconds);
	}

	@Override
	public int getKeepAliveSeconds() {
		return this.delegate.getKeepAliveSeconds();
	}

	@Override
	public void setQueueCapacity(int queueCapacity) {
		this.delegate.setQueueCapacity(queueCapacity);
	}

	@Override
	public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
		this.delegate.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
	}

	@Override
	public void setTaskDecorator(TaskDecorator taskDecorator) {
		this.delegate.setTaskDecorator(taskDecorator);
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
