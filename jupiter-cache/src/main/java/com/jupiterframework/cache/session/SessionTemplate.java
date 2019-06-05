package com.jupiterframework.cache.session;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;


/*
 * spring-session-data-redis因操作redis过于频繁影响性能，自定义封装session的操作
 */
/**
 * redis session 操作工具类
 */
@Slf4j
@Component
public class SessionTemplate {
	/** session header key */
	public static final String SESSION_KEY = "x-auth-token";
	private static final String SESSION_USER_ID_KEY = "LOGIN_USER_ID_SESSION_ID";
	public static final String HEADER_KEY = "LOGIN_USER";

	@Value("${server.session.timeout:1800}")
	private int sessionTimeout;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	@Resource(name = "redisTemplate")
	private HashOperations<String, Object, Object> hashOperations;

	private String getSessionId() {

		String sessionId = null;
		try {
			sessionId = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getHeader(SESSION_KEY);
		} catch (IllegalStateException e) {
			log.warn("Not http request ! ", e);
		}
		return sessionId;
	}

	/**
	 * 创建一个Session,并将参数转换为com.ueb.framework.domain.UserInfo
	 * 
	 * @param userInfo
	 * @return sessionId
	 */
	public String createSession(String userId) {
		String sessionId = UUID.randomUUID().toString();
		hashOperations.put(SESSION_USER_ID_KEY, userId, sessionId);
		try {
			((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse()
				.addHeader(SESSION_KEY, sessionId);
		} catch (IllegalStateException e) {
			log.warn("Not http request ! ", e);
		}

		return sessionId;
	}

	/**
	 * 指定的用户ID是否当前Session登录的
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isCurrentSession(Long userId) {
		String sessionId = String.valueOf(hashOperations.get(SESSION_USER_ID_KEY, userId));
		return (sessionId != null && sessionId.equals(this.getSessionId()));
	}

	public void removeAttribute(String name) {
		hashOperations.delete(getSessionId(), name);
	}

	/**
	 * 设置session，并重置session失效时间
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value) {
		redisTemplate.execute(new SessionCallback<Object>() {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String sessionId = getSessionId();
				operations.boundHashOps(sessionId).put(name, value);
				operations.expire(sessionId, sessionTimeout, TimeUnit.SECONDS);
				return null;
			}

		});
	}

	/**
	 * 获取属性并重设session失效时间
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name) {

		return redisTemplate.execute(new SessionCallback<T>() {

			@SuppressWarnings("rawtypes")
			@Override
			public T execute(RedisOperations operations) throws DataAccessException {
				String sessionId = getSessionId();
				if (sessionId == null)
					return null;
				operations.expire(sessionId, sessionTimeout, TimeUnit.SECONDS);
				return (T) operations.boundHashOps(sessionId).get(name);
			}

		});
	}

	/**
	 * Specifies the time, in seconds, between client requests before the
	 * servlet container will invalidate this session. A zero or negative time
	 * indicates that the session should never timeout.
	 * 
	 * @param interval
	 */
	public void setMaxInactiveInterval(int interval) {
		redisTemplate.expire(getSessionId(), interval, TimeUnit.SECONDS);
	}
}
