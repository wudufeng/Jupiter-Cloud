package com.jupiterframework.servicegovernance.monitor;

import com.jupiterframework.servicegovernance.monitor.domain.ErrorInstance;


/**
 * 服务异常监控
 * 
 * @author wudf
 *
 */
public interface ExceptionMonitor {

    void collect(ErrorInstance response);
}
