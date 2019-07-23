package com.jupiterframework.web.interceptor;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.jupiterframework.constant.CoreConstant;
import com.jupiterframework.context.ServiceContext;
import com.jupiterframework.util.NetUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * 服务初始化请求, 从header获取应用基本信息，设置sessionID
 * 
 * @author wudf
 *
 */
@Component
@Slf4j
public class ServiceInitialInterceptor extends GenericFilterBean {// filter比HandlerInterceptor优先执行
    private static final String[] ENDPOINTS = { "/health", "/logfile" };


    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            String uri = ((HttpServletRequest) req).getRequestURI();
            String contextPath = this.getServletContext().getContextPath();
            // exclude /health
            if (Arrays.binarySearch(ENDPOINTS, uri.substring(contextPath.length())) < 0) {
                String clientSide = ((HttpServletRequest) req).getHeader(CoreConstant.CONSUMER_SIDE);
                clientSide = clientSide == null ? "" : String.format("[%s]", clientSide);
                log.debug("receive request from {}:{}{}, uri {}", NetUtils.getRemoteAddr((HttpServletRequest) req), req.getRemotePort(), clientSide, uri);
            }
        }

        HttpServletRequest request = (HttpServletRequest) req;

        // 设置请求的源基本信息到线程变量里
        ServiceContext.setServiceCode(request.getRequestURI());

        chain.doFilter(request, response);

        ServiceContext.setServiceCode(null);
        ServiceContext.setRemoteServiceCode(null);
    }

}
