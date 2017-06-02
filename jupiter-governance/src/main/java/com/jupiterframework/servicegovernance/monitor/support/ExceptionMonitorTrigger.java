package com.jupiterframework.servicegovernance.monitor.support;

import java.util.Collection;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.annotation.Order;

import com.jupiterframework.exception.ExceptionEvent;
import com.jupiterframework.filter.ServiceResponseFilter;
import com.jupiterframework.servicegovernance.config.ServiceGovernanceProperties;
import com.jupiterframework.servicegovernance.monitor.ExceptionMonitor;
import com.jupiterframework.servicegovernance.monitor.domain.ErrorInstance;
import com.jupiterframework.web.handler.ServiceFailureHandler;
import com.jupiterframework.web.model.ServiceFailureResponse;

import lombok.AllArgsConstructor;


/**
 * 异常事件监控触发器
 * 
 * @author wudf
 *
 */
@AllArgsConstructor
@Order(1)
public class ExceptionMonitorTrigger implements ServiceResponseFilter, ExceptionEvent {

	private Collection<ExceptionMonitor> filters;

	private ServiceInstance server;

	private ServiceFailureHandler serviceErrorHandler;

	private ServiceGovernanceProperties properties;

	/** 服务同步调用返回异常响应的监控 */
	@Override
	public void doFilter(Object o) {
		if ((o instanceof ServiceFailureResponse) && (String.format("%s:%s", server.getHost(), server.getPort())
			.equals(((ServiceFailureResponse) o).getHost()))) {
			this.doCollect((ServiceFailureResponse) o);
		}

	}

	/** 异步调用抛出的异常监控 */
	@Override
	public void doEvent(Throwable e) {
		com.jupiterframework.web.model.ServiceFailureResponse sr = serviceErrorHandler.commonHandleException(null, e);
		this.doCollect(sr);
	}

	private void doCollect(ServiceFailureResponse sr) {
		if (!properties.getIgnoreErrorCode().contains(sr.getCode())) {
			ErrorInstance m = new ErrorInstance(sr.getCode(), sr.getMessage(), sr.getHost(), sr.getStackTrace(),
				sr.getTraceId(), sr.getPath());
			for (ExceptionMonitor filter : filters) {
				filter.collect(m);
			}
		}
	}
}
