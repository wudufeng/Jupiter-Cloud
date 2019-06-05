package com.jupiterframework.sequence.exception;

import com.jupiterframework.constant.ExceptionDefinition;


public enum SequenceExceptionCode implements ExceptionDefinition {
	UNDEFINED(700, "未定义序列[{}]"),

	OVER_MAXVALUE(701, "超出序列[{}]最大值"),

	;

	private SequenceExceptionCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	private int code;
	private String message;

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
