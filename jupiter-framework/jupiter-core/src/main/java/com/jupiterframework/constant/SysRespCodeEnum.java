package com.jupiterframework.constant;

/**
 * @author jupiter
 */

public enum SysRespCodeEnum implements ExceptionDefinition {
	SUCCESS(200, "success"),

	AUTH_SIGNATURE_NOTFOUND(401, "签名无效"), // 认证错误

	/** 404 */
	SERVICE_NOTFOUND(404, "服务不存在"),

	/** Feign Client 调用服务超时 */
	REQUEST_TIMEOUT(504, "服务超时"),

	UNKNOWN_ERR(9999, "服务异常");

	private final int code;
	private final String message;

	SysRespCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public int getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
