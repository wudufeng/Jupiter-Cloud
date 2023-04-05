package com.jupiterframework.servicegovernance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jupiterframework.servicegovernance.ServiceGovernanceAutoConfiguration.ExceptionMonitorTriggerConfigurer;
import com.jupiterframework.servicegovernance.ServiceGovernanceAutoConfiguration.WebMonitorConfigurer;
import com.jupiterframework.servicegovernance.config.ServiceGovernanceProperties;
import com.jupiterframework.servicegovernance.monitor.ExceptionMonitor;
import com.jupiterframework.servicegovernance.monitor.StatisticsService;
import com.jupiterframework.servicegovernance.monitor.support.ExceptionMonitorTrigger;
import com.jupiterframework.servicegovernance.monitor.support.ServiceMonitorInterceptor;
import com.jupiterframework.servicegovernance.monitor.support.Slf4jExceptionMonitor;
import com.jupiterframework.servicegovernance.monitor.support.StatisticsMonitorTrigger;
import com.jupiterframework.servicegovernance.monitor.support.StatisticsServiceImpl;


@Configuration
@EnableConfigurationProperties(ServiceGovernanceProperties.class)
@Import({ WebMonitorConfigurer.class, ExceptionMonitorTriggerConfigurer.class })
public class ServiceGovernanceAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(ExceptionMonitor.class)
    public ExceptionMonitor exceptionMonitor() {
        return new Slf4jExceptionMonitor();
    }


    @Bean
    @ConditionalOnMissingBean(StatisticsService.class)
    public StatisticsService businessEventMonitor() {
        return new StatisticsServiceImpl();
    }


    @Bean
    @ConditionalOnWebApplication
    public ServiceMonitorInterceptor serviceMonitorFilter(StatisticsService statisticsService,
            ServiceGovernanceProperties serviceGovernanceProperties) {
        return new ServiceMonitorInterceptor(statisticsService, serviceGovernanceProperties);
    }


    @Bean
    public StatisticsMonitorTrigger statisticsMonitorTrigger(
            ServiceGovernanceProperties serviceGovernanceProperties) {
        return new StatisticsMonitorTrigger(serviceGovernanceProperties.getStatisticsPeriod());
    }

    @Configuration
    @ConditionalOnClass({ com.jupiterframework.web.handler.ServiceFailureHandler.class })
    public static class ExceptionMonitorTriggerConfigurer {
        // 如果不额外定义一个class, 当参数类不存在时会报错
        @Bean
        public ExceptionMonitorTrigger exceptionMonitorTrigger(ApplicationContext applicationContext,
                ServiceInstance server,
                com.jupiterframework.web.handler.ServiceFailureHandler serviceErrorHandler,
                ServiceGovernanceProperties serviceGovernanceProperties) {
            return new ExceptionMonitorTrigger(
                applicationContext.getBeansOfType(ExceptionMonitor.class).values(), server,
                serviceErrorHandler, serviceGovernanceProperties);
        }

    }

    @Order(10000)
    @Configuration
    @ConditionalOnWebApplication
    public static class WebMonitorConfigurer implements WebMvcConfigurer {
        @Autowired
        private ApplicationContext applicationContext;


        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addWebRequestInterceptor(applicationContext.getBean(ServiceMonitorInterceptor.class));
        }

    }

}
