package com.jupiterframework.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceFailureResponse extends ServiceResponse<Object> {

	private static final long serialVersionUID = 8886191134284746607L;

	/** 错误详情 */
	private String exceptionDetail;
	/** 出错的服务路径 */
	private String url;
	/** 出错的服务IP与端口 */
	private String hostAndPort;

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
