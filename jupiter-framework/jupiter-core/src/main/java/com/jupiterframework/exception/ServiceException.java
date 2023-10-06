package com.jupiterframework.exception;

import lombok.Getter;
import lombok.Setter;


/**
 * 针对系统不可预测的服务异常(非业务异常)
 *
 * @author jupiter
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/** 错误码 */
	private int code;

	/** 异常信息 */
	private String message;

	/** 异常详情 */
	private String detail;

	// extends RuntimeException
	public ServiceException() {
		super();
	}

	public ServiceException(int code, String message) {
		this(code, message, null);
	}

	public ServiceException(int code, String message, Throwable e) {
		super(String.format("%s%s", code, message), e);
		this.setCode(code);
		this.setMessage(message);
	}

	@Override
	public String toString() {
		return String.format("%s :%s:%s", getClass().getName(), code, message == null ? "" : message);
	}
}
