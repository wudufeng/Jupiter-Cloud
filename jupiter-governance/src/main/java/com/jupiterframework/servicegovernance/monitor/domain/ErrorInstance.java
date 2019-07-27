package com.jupiterframework.servicegovernance.monitor.domain;

import java.io.Serializable;

import org.slf4j.MDC;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInstance implements Serializable {

    private static final long serialVersionUID = 8886191134284746607L;

    /** 响应码 */
    private int code;
    /** 响应信息 */
    private String message;
    /** 机器信息 */
    private String hostAndPort;
    /** 错误详情 */
    private String exceptionDetail;
    /** 跟踪号 */
    private String traceId;
    /** 出错的服务路径 */
    private String uri;


    public ErrorInstance(int code, String message, String exceptionDetail, String uri) {
        super();
        this.code = code;
        this.message = message;
        this.exceptionDetail = exceptionDetail;
        this.uri = uri;

        traceId = MDC.get(org.springframework.cloud.sleuth.Span.SPAN_ID_NAME);
    }

}
