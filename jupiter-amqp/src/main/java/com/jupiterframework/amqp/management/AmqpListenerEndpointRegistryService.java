package com.jupiterframework.amqp.management;

import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;

import com.jupiterframework.web.annotation.MicroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(tags = "MQ启停控制")
@MicroService
public class AmqpListenerEndpointRegistryService {

    @Autowired
    private RabbitListenerEndpointRegistry registry;

    @ApiOperation("启动mq监听")
    @PutMapping("/amqp/start")
    public void start() {

        if (!registry.isRunning()) {
            registry.start();
        }
    }


    @ApiOperation("停止mq监听")
    @PutMapping("/amqp/stop")
    public void stop() {
        registry.stop();// 停止MQ监听
    }
}
