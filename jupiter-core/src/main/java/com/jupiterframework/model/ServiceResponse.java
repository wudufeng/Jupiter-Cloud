package com.jupiterframework.model;

import java.io.Serializable;

import com.jupiterframework.constant.SysRespCodeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse<T> implements Serializable {
	private static final long serialVersionUID = 7138636080435478470L;

	/** 响应码 */
	private int code;

	/** 响应信息 */
	private String message;

	/** 跟踪号 */
	private String traceId;

	/** 消息体数据 */
	private T data;

	public ServiceResponse(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public ServiceResponse(T data) {
		super();
		this.code = SysRespCodeEnum.SUCCESS.getCode();
		this.message = SysRespCodeEnum.SUCCESS.getMessage();
		this.data = data;
	}

}
