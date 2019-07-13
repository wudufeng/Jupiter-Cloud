package com.ueb.framework.channel.core;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ueb.framework.channel.config.Channel;
import com.ueb.framework.channel.config.ChannelProperties;
import com.ueb.framework.channel.config.Service;
import com.ueb.framework.channel.pojo.MessageRequest;
import com.ueb.framework.channel.pojo.MessageResponse;

import io.swagger.annotations.Api;


@ConditionalOnWebApplication
@RestController
@Api(tags = "渠道接入服务")
public class ChannelService {

    @Autowired
    private ChannelProperties channelProperties;
    @Autowired
    private CommunicationProcessor communication;


    @GetMapping(value = "getChannelConfiguration")
    public Map<String, Channel> getChannelConfiguration() {
        return channelProperties.getChannelConfigurations();
    }


    @GetMapping(value = "getServiceConfiguration", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public Service getServiceConfiguration(@RequestParam String channelName, @RequestParam String serviceName) {
        return channelProperties.getChannelConfiguration(channelName).getServices().get(serviceName);
    }


    @PostMapping(value = "invoke", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public MessageResponse invoke(@RequestBody MessageRequest messageRequest) {
        return communication.request(messageRequest);
    }
}
