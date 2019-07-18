package com.jupiterframework.web.converter;

import javax.annotation.Resource;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jupiterframework.constant.SysRespCodeEnum;
import com.jupiterframework.context.ServiceContext;
import com.jupiterframework.exception.ServiceException;
import com.jupiterframework.model.ServiceFailureResponse;
import com.jupiterframework.util.ExceptionUtils;


@Component
public class ExceptionConverter {

	@Resource
	private ServiceInstance serverInstance;

	/** 系统级异常 */
	public ServiceFailureResponse buildServiceFailureResponse(SysRespCodeEnum sysResp, Throwable e) {
		return this.buildServiceFailureResponse(sysResp, null, e);
	}

	/** 系统级异常 */
	public ServiceFailureResponse buildServiceFailureResponse(SysRespCodeEnum sysResp, String exceptionClass,
			Throwable e) {
		ServiceFailureResponse sr = buildServiceFailureResponse();
		sr.setCode(sysResp.getCode());// 系统级异常不需要拼接应用ID
		sr.setMessage(sysResp.getMessage());
		sr.setException(exceptionClass == null ? e.getClass().getName() : exceptionClass);
		sr.setStackTrace(ExceptionUtils.getCauseMessage(e));
		return sr;
	}

	/** 参数异常 */
	public ServiceFailureResponse buildServiceFailureResponse(int code, String message, Throwable e) {
		ServiceFailureResponse sr = buildServiceFailureResponse();
		sr.setCode(code);
		sr.setMessage(message);
		sr.setException(e.getClass().getName());
		sr.setStackTrace(ExceptionUtils.getCauseMessage(e));
		return sr;
	}

	public ServiceFailureResponse buildServiceFailureResponse(ServiceException se) {
		if (se.getCause() instanceof ServiceException) {
			se = (ServiceException) se.getCause();// 通过反射或者ForkJoinTask调用会被包装一层
		}
		ServiceFailureResponse sr = null;
		if (se.getDetail() == null) {
			sr = buildServiceFailureResponse();
			sr.setMessage(se.getMessage());
			sr.setCode(se.getCode());
			sr.setException(se.getClass().getName());
			sr.setStackTrace(ExceptionUtils.getCauseMessage(se));
		} else {
			// FastJsonHttpMessageConverterEx 将原响应信息设置到detail里作为异常抛出，此处直接将其还原返回
			sr = JSON.parseObject(se.getDetail(), ServiceFailureResponse.class);
			sr.setCode(se.getCode());
		}
		return sr;
	}

	private ServiceFailureResponse buildServiceFailureResponse() {
		ServiceFailureResponse sr = new ServiceFailureResponse();
		sr.setPath(ServiceContext.getRemoteServiceCode() == null ? ServiceContext.getServiceCode()
				: ServiceContext.getRemoteServiceCode());
		sr.setTraceId(ServiceContext.getSpanId());
		sr.setHost(String.format("%s:%s", serverInstance.getHost(), serverInstance.getPort()));
		sr.setServiceId(serverInstance.getServiceId());
		return sr;
	}

}
