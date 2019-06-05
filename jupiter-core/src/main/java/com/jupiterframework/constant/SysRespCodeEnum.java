package com.jupiterframework.constant;

public enum SysRespCodeEnum implements ExceptionDefinition {
	SUCCESS(200, "success"),

	PARAMS_ERR(1, "参数错误"),

	METHOD_INVALID(2, "不支持的请求方式"), // 仅支持post请求

	AUTH_SIGNATURE_NOTFOUND(3, "签名无效"), // 认证错误

	/** 404 */
	SERVICE_NOTFOUND(4, "服务不存在"),

	/** Feign Client 调用服务超时 */
	REQUEST_TIMEOUT(5, "服务超时"),

	AUTH_SESSION_NOTFOUND(6, "登录已失效"),

	AUTH_INVALID(7, "无访问权限"),

	DATA_ACCESS_ERR(8, "系统异常"), // 数据库异常

	RESOURCE_INVALID(9, "服务不可用"), // 无可用服务

	SERVICE_REJECT(10, "服务不可用"), // 拒绝连接，关闭服务的瞬间，消费方未刷新

	REQUEST_LIMIT(11, "请求过于频繁"),

	DATA_NON_EXISTENT(12, "数据不存在"),

	SYSTEM_CONFIG_LACK(13, "系统配置缺失"),

	DUPLICATE_KEY_ERR(14, "数据已存在"),

	NOT_UNAUTHORIZED(15, "未通过授权"),

	AUTHORIZATION_EXPIRED(16, "授权已过期"),

	INVALID_SIGNATURE_STRING(17, "签名串无效"),

	UNKNOW_ERR(9999, "服务异常");

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
