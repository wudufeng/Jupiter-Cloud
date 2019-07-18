package com.jupiterframework.util;

public class ExceptionUtils {
	private ExceptionUtils() {
	}

	/**
	 * 获取最里层的cause
	 * 
	 * @param e
	 * @return
	 */
	public static Throwable getCause(Throwable e) {
		if (e.getCause() != null)
			return getCause(e.getCause());
		return e;
	}

	/**
	 * 获取最里层的cause与message
	 * 
	 * @param e
	 * @return
	 */
	public static String getCauseMessage(Throwable e) {
		Throwable cause = getCause(e);
		return String.format("%s:%s", cause.getClass().getName(), cause.getMessage());
	}

}
