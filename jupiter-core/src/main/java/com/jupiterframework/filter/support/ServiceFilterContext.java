package com.jupiterframework.filter.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.core.annotation.Order;

import com.jupiterframework.filter.ServiceRequestFilter;
import com.jupiterframework.filter.ServiceResponseFilter;


public class ServiceFilterContext {

    /**
     * 服务请求前处理
     */
    private List<ServiceRequestFilter> svcReqFilters = new ArrayList<>(1);

    /**
     * 服务处理后拦截处理
     */
    private List<ServiceResponseFilter> svcRespFilters = new ArrayList<>(1);


    public ServiceFilterContext(List<ServiceRequestFilter> svcReqFilters, List<ServiceResponseFilter> svcRespFilters) {
        super();
        Collections.sort(svcReqFilters, new Comparator<ServiceRequestFilter>() {
            @Override
            public int compare(ServiceRequestFilter o1, ServiceRequestFilter o2) {
                return o1.getClass().getAnnotation(Order.class).value() - o2.getClass().getAnnotation(Order.class).value();
            }
        });

        Collections.sort(svcRespFilters, new Comparator<ServiceResponseFilter>() {
            @Override
            public int compare(ServiceResponseFilter o1, ServiceResponseFilter o2) {
                return o1.getClass().getAnnotation(Order.class).value() - o2.getClass().getAnnotation(Order.class).value();
            }
        });
        this.svcReqFilters.addAll(svcReqFilters);
        this.svcRespFilters.addAll(svcRespFilters);
    }


    public List<ServiceRequestFilter> getSvcReqFilters() {
        return svcReqFilters;
    }


    public List<ServiceResponseFilter> getSvcRespFilters() {
        return svcRespFilters;
    }

}
