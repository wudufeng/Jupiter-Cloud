package com.jupiterframework.web.interceptor;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


/**
 * 打印方法请求参数
 * 
 * @author wudf
 *
 */
@Slf4j
@Aspect
@Component
@Order(-10000) // 切面作用在参数校验之后，事务之前，如果不指定则会包含在事务里
public class ArgumentInterceptor {

	public ArgumentInterceptor() {
	}

	/**
	 * 异常抛错前打印方法请求参数
	 * 
	 * @param jp
	 * @return
	 * @throws Throwable
	 */
	@Around("@within(com.jupiterframework.web.annotation.MicroService)")
	public Object aspect(ProceedingJoinPoint jp) throws Throwable {
		if (log.isDebugEnabled()) {
			Signature sign = jp.getSignature();
			// 需要保证参数都使用了@Data注解，或者重写了toString方法
			log.debug(" {}.{} parameter {}", sign.getDeclaringTypeName(), sign.getName(),
				Arrays.toString(jp.getArgs()));
		}

		try {
			return jp.proceed();
		} catch (Exception e) {
			Signature sign = jp.getSignature();
			// 需要保证参数都使用了@Data注解，或者重写了toString方法
			log.warn(" {}.{} execute failed , parameter :  {}", sign.getDeclaringTypeName(), sign.getName(),
				Arrays.toString(jp.getArgs()));
			throw e;
		}
	}
}
