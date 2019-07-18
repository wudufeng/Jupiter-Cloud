package com.jupiterframework.web.handler;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.jupiterframework.model.ServiceResponse;
import com.jupiterframework.web.annotation.MicroService;


/**
 * 将返回的对象包装为ServiceResponse
 * 
 * @author wudf
 *
 */
@ControllerAdvice(annotations = MicroService.class)
public class ServiceSuccessHandler implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		Class<?> type = returnType.getMethod().getReturnType();
		return !ServiceResponse.class.isAssignableFrom(type) && !byte[].class.isAssignableFrom(type);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		return new ServiceResponse<>(body);
	}

}
