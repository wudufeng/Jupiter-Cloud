package com.jupiterframework.web.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.DynamicMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jupiterframework.constant.CoreConstant;
import com.jupiterframework.constant.SysRespCodeEnum;
import com.jupiterframework.context.ServiceContext;
import com.jupiterframework.exception.RemoteExecutionException;
import com.jupiterframework.model.ServiceResponse;
import com.jupiterframework.util.BeanUtils;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import lombok.extern.slf4j.Slf4j;


/**
 * 使用feignclient调用前会执行此类
 * 
 * @author wudf
 *
 */
@Component
@Slf4j
public class FeignClientInterceptor implements ImportBeanDefinitionRegistrar, feign.RequestInterceptor,
		feign.codec.Decoder, ApplicationContextAware {

	@Value("${spring.application.name}")
	private String applicationName;

	private List<ClientInterceptor> clientInterceptor = new ArrayList<>(0);

	private static final String RET_CODE_KEY = "code";
	private static final String RET_MSG_KEY = "message";
	private static final String BODY_KEY = "data";

	private static final ThreadLocal<String[]> REMOTE_EXCEPTION = new ThreadLocal<>();

	@Override
	public void apply(feign.RequestTemplate template) {
		template.header(CoreConstant.CONSUMER_SIDE, applicationName);

		ServiceContext.setRemoteServiceCode(template.url());

		for (ClientInterceptor c : clientInterceptor) {
			c.apply(template);
		}

		log.debug("request url {}{}", template.url(), template.queryLine());
		if (log.isDebugEnabled() && template.body() != null) {
			log.debug("request body {}", JSON.parse(template.body()));
		}
	}

	@Override
	public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
		// feignclient 调用前会设置值,如果接收到后不再使用此值
		ServiceContext.setRemoteServiceCode(null);

		// 接口声明返回类型是ServiceResponse，则不做任何处理
		if ((type instanceof Class && ServiceResponse.class.isAssignableFrom((Class<?>) type))
				|| (type instanceof ParameterizedType && ServiceResponse.class
					.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType()))) {
			return JSON.parseObject(response.body().asInputStream(), BeanUtils.FASTJSON_CONFIG.getCharset(), type,
				BeanUtils.FASTJSON_CONFIG.getFeatures());
		}

		try {
			InputStream in = response.body().asInputStream();
			JSONObject jsonObj = JSON.parseObject(in, BeanUtils.FASTJSON_CONFIG.getCharset(), JSONObject.class,
				BeanUtils.FASTJSON_CONFIG.getFeatures());

			log.debug("receive response message : {}", jsonObj);

			if (jsonObj.containsKey(RET_CODE_KEY)) {
				String retCode = jsonObj.getString(RET_CODE_KEY);
				if (!String.valueOf(SysRespCodeEnum.SUCCESS.getCode()).equals(retCode)) {
					log.info("server sign execute fail , receive {} ", jsonObj);
					// 如果直接在此处抛异常会导致hystrix触发熔断
					REMOTE_EXCEPTION
						.set(new String[] { retCode, jsonObj.getString(RET_MSG_KEY), jsonObj.toJSONString() });
					return null;
				}
				if (type instanceof Class && BeanUtils.isPrimitive((Class<?>) type)) {
					if (Date.class.isAssignableFrom((Class<?>) type)) {
						return DateUtils.parseDate(jsonObj.getString(BODY_KEY), BeanUtils.DATE_FORMAT);
					} else if (Long.class.isAssignableFrom((Class<?>) type)) {
						return Long.parseLong(jsonObj.getString(BODY_KEY));
					}
					return jsonObj.get(BODY_KEY);
				}
				return JSON.parseObject(jsonObj.getString(BODY_KEY), type, BeanUtils.FASTJSON_CONFIG.getFeatures());
			} else {
				return jsonObj.toJavaObject(type);
			}
		} catch (JSONException ex) {
			throw new DecodeException("JSON parse error: " + ex.getMessage(), ex);
		} catch (ParseException e) {
			throw new DecodeException("Date parse error :", e);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		List<ClientInterceptor> clients =
				new ArrayList<>(applicationContext.getBeansOfType(ClientInterceptor.class).values());
		Collections.sort(clients, new Comparator<ClientInterceptor>() {
			@Override
			public int compare(ClientInterceptor o1, ClientInterceptor o2) {
				return o1.getClass().getAnnotation(Order.class).value()
						- o2.getClass().getAnnotation(Order.class).value();
			}
		});
		clientInterceptor.addAll(clients);
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Map<String, Object> attrs = importingClassMetadata.getAnnotationAttributes(EnableFeignClients.class.getName());

		Set<String> basePackages = new HashSet<>();
		for (String pkg : (String[]) attrs.get("basePackages")) {
			if (StringUtils.hasText(pkg)) {
				basePackages.add(pkg);
			}
		}

		RootBeanDefinition advisorBeanDevinition = new RootBeanDefinition(FeignClientResponseAdvisor.class);
		advisorBeanDevinition.setScope(BeanDefinition.SCOPE_SINGLETON);
		advisorBeanDevinition.setSynthetic(true);
		advisorBeanDevinition.getPropertyValues().add("basePackages", basePackages);

		BeanDefinitionHolder holder =
				new BeanDefinitionHolder(advisorBeanDevinition, "feignClientResponseAdvisor", new String[] {});
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
	}

	/**
	 * 用于使用feignclient调用服务后，进行业务判断，抛出业务异常
	 */
	public static class FeignClientResponseAdvisor extends AbstractPointcutAdvisor {
		private static final long serialVersionUID = -243997959201304289L;

		private transient Pointcut pointcut;
		private transient Advice advice;

		private Set<String> basePackages;// SpiClients.basePackages

		public FeignClientResponseAdvisor() {
			this.pointcut = new FeignResponsePointcut();
			this.advice = new FeignResponseInterceptor();
			this.setOrder(Ordered.LOWEST_PRECEDENCE);
		}

		public void setBasePackages(Set<String> basePackages) {
			this.basePackages = basePackages;
		}

		@Override
		public Pointcut getPointcut() {
			return pointcut;
		}

		@Override
		public Advice getAdvice() {
			return advice;
		}

		private class FeignResponsePointcut extends DynamicMethodMatcherPointcut {

			@Override
			public boolean matches(Method method, Class<?> targetClass, Object... args) {
				return true;
			}

			@Override
			public ClassFilter getClassFilter() {
				return new ClassFilter() {
					@Override
					public boolean matches(Class<?> clazz) {
						boolean flag = new AnnotationClassFilter(FeignClient.class, true).matches(clazz);
						if (flag) {
							for (Class<?> ifc : clazz.getInterfaces()) {
								if (basePackages.contains(ifc.getPackage().getName()))
									return true;
							}

						}
						return false;
					}
				};
			}
		}

		private class FeignResponseInterceptor implements MethodInterceptor {
			@Override
			public Object invoke(MethodInvocation invocation) throws Throwable {

				Object result = invocation.proceed();

				String[] e = FeignClientInterceptor.REMOTE_EXCEPTION.get();
				if (e != null) {
					FeignClientInterceptor.REMOTE_EXCEPTION.set(null);

					RemoteExecutionException se = new RemoteExecutionException(Integer.parseInt(e[0]), e[1]);
					se.setDetail(e[2]);

					throw se;
				}
				return result;
			}
		}

	}

}