package com.jupiterframework.servicegovernance.monitor.support;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.jupiterframework.servicegovernance.config.ServiceGovernanceProperties;
import com.jupiterframework.servicegovernance.monitor.StatisticsService;
import com.jupiterframework.servicegovernance.monitor.domain.StatisticsDimension;
import com.jupiterframework.servicegovernance.monitor.domain.StatisticsInstance;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * web应用拦截，触发数据采集度量统计<br/>
 * Zuul不会进入此拦截
 * 
 * @author wudf
 *
 */
@AllArgsConstructor
@Slf4j
public class ServiceMonitorInterceptor implements WebRequestInterceptor {
    private static final String ELAPSED_BEGIN_KEY = "ELAPSED_BEGIN";
    private static final String CONCURRENT_KEY = "CONCURRENT";

    private StatisticsService statisticsService;

    private ServiceGovernanceProperties properties;


    @Override
    public void preHandle(WebRequest request) throws Exception {
        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();

        request.setAttribute(ELAPSED_BEGIN_KEY, System.nanoTime(), WebRequest.SCOPE_REQUEST);
        request.setAttribute(CONCURRENT_KEY, statisticsService.incrementAndGetConcurrent(uri), WebRequest.SCOPE_REQUEST);
    }


    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {

    }


    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {

        String uri = ((ServletWebRequest) request).getRequest().getRequestURI();
        Throwable t = ex;
        if (t == null) {
            t = (Throwable) request.getAttribute("org.springframework.boot.autoconfigure.web.DefaultErrorAttributes.ERROR", WebRequest.SCOPE_REQUEST);
        }
        if (t == null) {
            t = (Throwable) request.getAttribute("javax.servlet.error.exception", WebRequest.SCOPE_REQUEST);
        }
        statisticsService.collect(new StatisticsDimension(uri),
            new StatisticsInstance(t == null, elapsed(request, uri), (int) request.getAttribute(CONCURRENT_KEY, WebRequest.SCOPE_REQUEST)));

        statisticsService.decrementConcurrent(uri);
    }


    private long elapsed(WebRequest request, String uri) {
        long elapsed = System.nanoTime() - (long) request.getAttribute(ELAPSED_BEGIN_KEY, WebRequest.SCOPE_REQUEST);

        if (elapsed < properties.getInfoElapsed()) {// 基于时间复杂度
            log.debug("{} 耗时 {} (纳秒)", uri, elapsed);
        } else if (elapsed < properties.getWarnElapsed()) {
            log.info("{} 耗时 {} (纳秒)", uri, elapsed);
        } else {
            log.warn("{} 耗时 {} (纳秒)", uri, elapsed);
        }

        return elapsed;
    }

}
