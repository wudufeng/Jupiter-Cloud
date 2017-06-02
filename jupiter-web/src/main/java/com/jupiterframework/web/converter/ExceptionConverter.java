package com.jupiterframework.web.converter;

import javax.annotation.Resource;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jupiterframework.constant.SysRespCodeEnum;
import com.jupiterframework.context.ServiceContext;
import com.jupiterframework.exception.ServiceException;
import com.jupiterframework.model.BaseInfo;
import com.jupiterframework.util.ExceptionUtils;
import com.jupiterframework.web.model.ServiceFailureResponse;


@Component
public class ExceptionConverter {

	@Resource
	private BaseInfo baseInfo;

	@Resource
	private ServiceInstance serverInstance;

	/** 系统级异常 */
	public ServiceFailureResponse buildServiceFailureResponse(SysRespCodeEnum sysResp, Throwable exceptionDetail) {
		ServiceFailureResponse sr = buildServiceFailureResponse();
		sr.setCode(sysResp.getCode());// 系统级异常不需要拼接应用ID
		sr.setMessage(sysResp.getMessage());
		sr.setException(exceptionDetail.getClass().getName());
		sr.setStackTrace(ExceptionUtils.getCause(exceptionDetail).getMessage());
		return sr;
	}

	/** 系统级异常 */
	public ServiceFailureResponse buildServiceFailureResponse(SysRespCodeEnum sysResp, String exceptionDetail) {
		ServiceFailureResponse sr = buildServiceFailureResponse();
		sr.setCode(sysResp.getCode());// 系统级异常不需要拼接应用ID
		sr.setMessage(sysResp.getMessage());
		sr.setException(exceptionDetail);
		sr.setStackTrace(exceptionDetail);
		return sr;
	}

	/** 参数异常 */
	public ServiceFailureResponse buildServiceFailureResponse(int code, String message) {
		ServiceFailureResponse sr = buildServiceFailureResponse();
		sr.setCode(code);
		sr.setMessage(message);
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
