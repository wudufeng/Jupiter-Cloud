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

import com.alibaba.fastjson.JSON;
import com.jupiterframework.constant.CoreConstant;
import com.jupiterframework.context.ServiceContext;
import com.jupiterframework.model.BaseInfo;
import com.jupiterframework.model.UserInfo;
import com.jupiterframework.util.NetUtils;
import com.jupiterframework.util.SessionUtils;

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
	private final static String[] ENDPOINTS = { "/health", "/logfile" };

	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (log.isDebugEnabled()) {
			String uri = ((HttpServletRequest) req).getRequestURI();
			String contextPath = this.getServletContext().getContextPath();
			// exclude /health
			if (Arrays.binarySearch(ENDPOINTS, uri.substring(contextPath.length())) < 0) {
				String clientSide = ((HttpServletRequest) req).getHeader(CoreConstant.CONSUMER_SIDE);
				clientSide = clientSide == null ? "" : String.format("[%s]", clientSide);
				log.debug("receive request from {}:{}{}, uri {}", NetUtils.getRemoteAddr((HttpServletRequest) req),
					req.getRemotePort(), clientSide, uri);
			}
		}

		HttpServletRequest request = (HttpServletRequest) req;
		String userSessionStr = request.getHeader(UserInfo.HEADER_KEY);
		UserInfo user = null;
		if (userSessionStr != null) {
			user = JSON.parseObject(userSessionStr, UserInfo.class);
		}
		if (user == null) {
			user = new UserInfo();
		}
		user.setSessionId(request.getHeader(CoreConstant.SESSION_KEY));
		SessionUtils.setUserInfo(user);

		// 设置请求的源基本信息到线程变量里
		String baseInfoStr = request.getHeader(BaseInfo.HEADER_KEY);
		BaseInfo baseInfo = null;
		if (baseInfoStr != null) {
			baseInfo = JSON.parseObject(baseInfoStr, BaseInfo.class);

		}
		SessionUtils.setBaseInfo(baseInfo);
		ServiceContext.setServiceCode(request.getRequestURI());

		chain.doFilter(request, response);

		ServiceContext.setServiceCode(null);
		ServiceContext.setRemoteServiceCode(null);
	}

}
