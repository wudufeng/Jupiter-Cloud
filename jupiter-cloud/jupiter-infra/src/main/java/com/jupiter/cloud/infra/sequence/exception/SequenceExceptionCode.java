package com.jupiter.cloud.infra.sequence.exception;

import com.jupiterframework.constant.ExceptionDefinition;


/**
 * @author jupiter
 */

public enum SequenceExceptionCode implements ExceptionDefinition {
	UNDEFINED(700, "未定义序列[{}]"),

	OVER_MAXVALUE(701, "超出序列[{}]最大值"),

	ERR_MAXVALUE(702, "最大值{}与最小值{}范围太小，不能少于10"),

	;

	SequenceExceptionCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	private final int code;
	private final String message;

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
