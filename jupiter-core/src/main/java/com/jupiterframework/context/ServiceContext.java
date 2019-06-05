package com.jupiterframework.context;

import org.slf4j.MDC;


public class ServiceContext {
    public static final String TRACE_ID_MDC_KEY = "X-B3-TraceId";
    /** 服务编码，web项目就是uri */
    private static final ThreadLocal<String> SERVICE_CODE = new ThreadLocal<>();

    /** 消费者调用远程服务的uri */
    private static final ThreadLocal<String> REMOTE_SERVICE_CODE = new ThreadLocal<>();


    private ServiceContext() {
    }


    /**
     * 获取跟踪号
     * 
     * @return
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_MDC_KEY);
    }


    /**
     * 
     * @return
     */
    public static String getParentId() {
        return MDC.get("X-B3-ParentSpanId");
    }


    /**
     * 
     * @return
     */
    public static String getSpanId() {
        return MDC.get("X-B3-SpanId");
    }


    public static String getServiceCode() {
        return SERVICE_CODE.get();
    }


    public static void setServiceCode(String serviceCode) {
        SERVICE_CODE.set(serviceCode);
    }


    public static String getRemoteServiceCode() {
        return REMOTE_SERVICE_CODE.get();
    }


    /** feignclient拦截器调用前会设置 */
    public static void setRemoteServiceCode(String remoteServiceCode) {
        REMOTE_SERVICE_CODE.set(remoteServiceCode);
    }
}
