package com.jupiterframework.channel.core;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jupiterframework.channel.config.Channel;
import com.jupiterframework.channel.config.Service;
import com.jupiterframework.channel.pojo.MessageRequest;
import com.jupiterframework.channel.pojo.MessageResponse;

import io.swagger.annotations.Api;


@ConditionalOnWebApplication
@RestController
@Api(tags = "渠道接入服务")
public class ChannelService {

    @Resource
    private ChannelConfigurationContext channelProperties;
    @Autowired
    private CommunicationProcessor communication;


    @GetMapping(value = "/channel/configuration")
    public Map<String, Channel> getChannelConfiguration() {
        return channelProperties.getChannelConfigurations();
    }


    @GetMapping(value = "/channel/service/configuration", produces = { MediaType.APPLICATION_JSON_VALUE })
    public Service getServiceConfiguration(@RequestParam String channelName,
            @RequestParam String serviceName) {
        return channelProperties.getChannelConfiguration(channelName).getServices().get(serviceName);
    }


    @PostMapping(value = "/channel/invoke", produces = { MediaType.APPLICATION_JSON_VALUE })
    public MessageResponse invoke(@RequestBody MessageRequest messageRequest) {
        return communication.request(messageRequest);
    }
}
