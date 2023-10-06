package com.jupiter.cloud.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;

import java.net.URI;

/**
 * @author jupiter
 */
@EnableWebFluxSecurity
@Configuration
public class WebSecurityConfiguration {

    @Order(100)
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AdminServerProperties adminServerProperties) {
        String adminContextPath = adminServerProperties.getContextPath();

        String[] whiteList = new String[]{
                adminContextPath + "/assets/**",
                adminContextPath + "/login",
                adminContextPath + "/instances/**",
                adminContextPath + "/actuator/**"
        };
        RedirectServerLogoutSuccessHandler loginRedirect = new RedirectServerLogoutSuccessHandler();
        loginRedirect.setLogoutSuccessUrl(URI.create(adminContextPath));
        http.headers(headersConfigure -> headersConfigure.frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable))
                .authorizeExchange(authorize -> authorize.pathMatchers(whiteList).permitAll().anyExchange().authenticated())
                .formLogin(formLoginConfigurer -> formLoginConfigurer.loginPage(adminContextPath + "/login")
                        .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler(adminContextPath)))
                .logout(logoutConfigurer -> logoutConfigurer.logoutUrl(adminContextPath + "/logout").logoutSuccessHandler(loginRedirect))
			    .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
        ;
        return http.build();
    }

}
