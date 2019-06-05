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

}
