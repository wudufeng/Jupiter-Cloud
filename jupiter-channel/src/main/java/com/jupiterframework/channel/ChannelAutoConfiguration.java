package com.jupiterframework.channel;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.jupiterframework.channel.config.ChannelProperties;


@EnableConfigurationProperties(ChannelProperties.class)
@Configuration
@ComponentScan
public class ChannelAutoConfiguration {

}
