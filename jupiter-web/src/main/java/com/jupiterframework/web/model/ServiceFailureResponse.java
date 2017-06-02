package com.jupiterframework.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceFailureResponse extends ServiceResponse<Object> {

	private static final long serialVersionUID = 8886191134284746607L;

	/** 错误详情 */
	private String exception;
	/** 源异常堆栈信息 */
	private String stackTrace;

	/** 应用名称 */
	private String serviceId;

	/** 出错的服务路径 */
	private String path;
	/** 出错的服务IP与端口 */
	private String host;

	public ServiceFailureResponse() {
		super();
		setData(null);
	}

	public ServiceFailureResponse(int code, String msg) {
		super(code, msg);
	}

	public ServiceFailureResponse(Object data) {
		super(data);
	}

}
