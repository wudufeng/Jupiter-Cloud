package com.jupiterframework.web.interceptor;

public interface ClientInterceptor {

    public void apply(feign.RequestTemplate template);
}
