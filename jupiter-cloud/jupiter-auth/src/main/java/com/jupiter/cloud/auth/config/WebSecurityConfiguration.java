package com.jupiter.cloud.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 服务安全相关配置
 * @author jupiter
 */
@EnableWebSecurity
public class WebSecurityConfiguration {

	/**
	 * spring security 默认的安全策略
	 */
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers("/token/*")
						.permitAll()// 开放自定义的部分端点
						.anyRequest()
						.authenticated())
				.formLogin(Customizer.withDefaults());
		return http.build();
	}

	/**
	 * 暴露静态资源
	 */
	@Bean
	@Order(0)
	SecurityFilterChain resources(HttpSecurity http) throws Exception {
		return http.securityMatchers(matchers -> matchers.requestMatchers("/actuator/**", "/css/**", "/error"))
				.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
				.requestCache(AbstractHttpConfigurer::disable)
				.securityContext(AbstractHttpConfigurer::disable)
				.sessionManagement(AbstractHttpConfigurer::disable)
				.build();
	}
}
