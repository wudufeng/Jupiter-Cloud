package com.jupiterframework.servicegovernance.monitor.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jupiterframework.servicegovernance.constant.ServiceGovernanceConstants;
import com.jupiterframework.servicegovernance.monitor.ExceptionMonitor;
import com.jupiterframework.servicegovernance.monitor.domain.ErrorInstance;


/**
 * 通过输出异常数据到日志文件，采集日志文件数据到监控系统
 * 
 * @author wudf
 *
 */
public class Slf4jExceptionMonitor implements ExceptionMonitor {
	private static final Logger LOG = LoggerFactory.getLogger(ServiceGovernanceConstants.ERROR_LOGGER_NAME);

	@Override
	public void collect(ErrorInstance sr) {
		// 错误码 TraceId host:port uri 自定义的错误提示信息 异常堆栈信息
		LOG.trace("{}|{}|{}|{}|{}|{}", sr.getCode(), sr.getTraceId(), sr.getHostAndPort(), sr.getUri(), sr.getMessage(),
			sr.getExceptionDetail());
	}

}
