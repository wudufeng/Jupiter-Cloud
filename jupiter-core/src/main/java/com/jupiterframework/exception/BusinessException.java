package com.jupiterframework.exception;

import org.slf4j.helpers.MessageFormatter;

import com.jupiterframework.constant.ExceptionDefinition;


public class BusinessException extends ServiceException {

	private static final long serialVersionUID = -4400254935326910664L;

	public BusinessException() {
		super();
	}

	public BusinessException(ExceptionDefinition exceptionDefinition) {
		super(exceptionDefinition.getCode(), exceptionDefinition.getMessage());
	}

	/**
	 * 
	 * @param exceptionDefinition
	 * @param parameters 替换错误信息{}的符号
	 */
	public BusinessException(ExceptionDefinition exceptionDefinition, Object... parameters) {
		super(exceptionDefinition.getCode(),
			MessageFormatter.arrayFormat(exceptionDefinition.getMessage(), parameters).getMessage());
	}

	protected BusinessException(int code, String message) {
		super(code, message);
	}

	protected BusinessException(int code, String message, Throwable e) {
		super(code, message, e);
	}

}
