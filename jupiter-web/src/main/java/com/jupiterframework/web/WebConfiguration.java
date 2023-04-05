package com.jupiterframework.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jupiterframework.filter.ServiceRequestFilter;
import com.jupiterframework.filter.ServiceResponseFilter;
import com.jupiterframework.filter.support.ServiceFilterContext;
import com.jupiterframework.web.interceptor.ServiceInterceptor;


@Configuration
@ComponentScan
@ConditionalOnWebApplication
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Set<Entry<String, ServiceInterceptor>> set =
                applicationContext.getBeansOfType(ServiceInterceptor.class).entrySet();
        List<ServiceInterceptor> list = new ArrayList<>(set.size());

        for (Entry<String, ServiceInterceptor> entry : set) {
            list.add(entry.getValue());
        }

        Collections.sort(list, new Comparator<ServiceInterceptor>() {
            @Override
            public int compare(ServiceInterceptor o1, ServiceInterceptor o2) {

                return o1.getClass().getAnnotation(Order.class).value()
                        - o2.getClass().getAnnotation(Order.class).value();
            }

        });

        for (ServiceInterceptor s : list) {
            registry.addInterceptor(s);
        }
    }


    @Bean
    public ServiceFilterContext serviceFilterContext(ApplicationContext applicationContext) {

        List<ServiceRequestFilter> svcReqFilters = new ArrayList<>(1);
        List<ServiceResponseFilter> svcRespFilters = new ArrayList<>(1);

        Set<Entry<String, ServiceRequestFilter>> reqSet =
                applicationContext.getBeansOfType(ServiceRequestFilter.class).entrySet();
        for (Entry<String, ServiceRequestFilter> entry : reqSet) {
            svcReqFilters.add(entry.getValue());
        }

        // 设置response filter处理器
        Set<Entry<String, ServiceResponseFilter>> respSet =
                applicationContext.getBeansOfType(ServiceResponseFilter.class).entrySet();
        for (Entry<String, ServiceResponseFilter> entry : respSet) {
            svcRespFilters.add(entry.getValue());
        }

        return new ServiceFilterContext(svcReqFilters, svcRespFilters);
    }
}
