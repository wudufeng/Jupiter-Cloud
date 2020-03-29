package com.jupiterframework.amqp;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.jupiterframework.amqp.core.AmqpTemplate;
import com.jupiterframework.amqp.core.AmqpTransactionListener;
import com.jupiterframework.amqp.core.RetryRequeueRecoverer;
import com.jupiterframework.amqp.core.TraceRabbitListenerContainerFactory;
import com.jupiterframework.amqp.management.AmqpListenerEndpointRegistryService;
import com.jupiterframework.transaction.TransactionDetermine;


@Configuration
@AutoConfigureAfter({ DataSourceAutoConfiguration.class })
public class AmqpAutoConfiguration {

    private final ObjectProvider<MessageConverter> messageConverter;

    private final RabbitProperties properties;

    public AmqpAutoConfiguration(ObjectProvider<MessageConverter> messageConverter, RabbitProperties properties) {
        this.messageConverter = messageConverter;
        this.properties = properties;
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public MessageRecoverer messageRecoverer(org.springframework.amqp.core.AmqpTemplate amqpTemplate) {
        return new RetryRequeueRecoverer(amqpTemplate);
    }


    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory, TransactionDetermine transactionDetermine) {
        AmqpTemplate rabbitTemplate = new AmqpTemplate(connectionFactory, transactionDetermine);
        MessageConverter mc = this.messageConverter.getIfUnique();
        if (mc != null) {
            rabbitTemplate.setMessageConverter(mc);
        }
        rabbitTemplate.setMandatory(determineMandatoryFlag());
        RabbitProperties.Template templateProperties = this.properties.getTemplate();
        RabbitProperties.Retry retryProperties = templateProperties.getRetry();
        if (retryProperties.isEnabled()) {
            rabbitTemplate.setRetryTemplate(createRetryTemplate(retryProperties));
        }
        if (templateProperties.getReceiveTimeout() != null) {
            rabbitTemplate.setReceiveTimeout(templateProperties.getReceiveTimeout());
        }
        if (templateProperties.getReplyTimeout() != null) {
            rabbitTemplate.setReplyTimeout(templateProperties.getReplyTimeout());
        }
        return rabbitTemplate;
    }


    private boolean determineMandatoryFlag() {
        Boolean mandatory = this.properties.getTemplate().getMandatory();
        return (mandatory != null ? mandatory : this.properties.isPublisherReturns());
    }


    private RetryTemplate createRetryTemplate(RabbitProperties.Retry properties) {
        RetryTemplate template = new RetryTemplate();
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(properties.getMaxAttempts());
        template.setRetryPolicy(policy);
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(properties.getInitialInterval());
        backOffPolicy.setMultiplier(properties.getMultiplier());
        backOffPolicy.setMaxInterval(properties.getMaxInterval());
        template.setBackOffPolicy(backOffPolicy);
        return template;
    }


    @Bean
    @ConditionalOnMissingBean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory,
            ThreadPoolTaskExecutor threadPoolExecutor) {
        SimpleRabbitListenerContainerFactory factory = new TraceRabbitListenerContainerFactory();
        factory.setTaskExecutor(threadPoolExecutor);
        factory.setMessageConverter(messageConverter());
        configurer.configure(factory, connectionFactory);
        return factory;
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
