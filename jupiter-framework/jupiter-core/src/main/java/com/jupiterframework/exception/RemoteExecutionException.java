package com.jupiterframework.exception;

/**
 * 远程服务执行失败
 * 
 * @author wudf
 *
 */
public class RemoteExecutionException extends ServiceException {

	private static final long serialVersionUID = -4400254935326910664L;

	public RemoteExecutionException() {
		super();
	}

	public RemoteExecutionException(int code, String message) {
		super(code, message);
	}

	public RemoteExecutionException(int code, String message, Throwable e) {
		super(code, message, e);
	}

}
