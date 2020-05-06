package com.jupiterframework.amqp;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jupiterframework.amqp.core.AmqpTemplate;
import com.jupiterframework.amqp.core.AmqpTransactionListener;
import com.jupiterframework.amqp.core.RetryRequeueRecoverer;
import com.jupiterframework.amqp.management.AmqpListenerEndpointRegistryService;


@Configuration
@AutoConfigureAfter({ DataSourceAutoConfiguration.class })
public class AmqpAutoConfiguration {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public MessageRecoverer messageRecoverer(org.springframework.amqp.core.AmqpTemplate amqpTemplate) {
        return new RetryRequeueRecoverer(amqpTemplate);
    }


    @Bean
    public AmqpTransactionListener amqpTransactionListener(AmqpTemplate amqpTemplate) {
        return new AmqpTransactionListener(amqpTemplate);
    }


    @Bean
    @ConditionalOnWebApplication
    public AmqpListenerEndpointRegistryService amqpListenerEndpointRegistryService() {
        return new AmqpListenerEndpointRegistryService();
    }


    @Bean
    public org.springframework.amqp.rabbit.core.RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
