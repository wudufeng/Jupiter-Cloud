package com.jupiterframework.amqp.core;

import org.slf4j.MDC;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.jupiterframework.transaction.TransactionDetermine;


/**
 * 支持traceID session的传递
 * 
 * @author wudf
 *
 */
public class AmqpTemplate extends RabbitTemplate {
    private TransactionDetermine transactionDetermine;

    @Value("${spring.application.name}")
    private String applicationName;

    public AmqpTemplate(ConnectionFactory connectionFactory, TransactionDetermine transactionDetermine) {
        super(connectionFactory);
        this.transactionDetermine = transactionDetermine;
    }


    @Override
    public void send(String exchange, String routingKey, Message message, CorrelationData correlationData) throws AmqpException {
        if (correlationData == null) {
            correlationData = new CorrelationData(MDC.get(Span.SPAN_ID_NAME));// 跟踪号
            // 判断是否在数据库事务里
            if (transactionDetermine.isActualTransactionActive()) {
                // 拦截消息发送，在事务提交后才发送
                MessageThreadLocalContext.registryProducerMessage(new MessageData(exchange, routingKey, message, correlationData));
                return;
            }
        }

        message.getMessageProperties().setHeader(Span.TRACE_ID_NAME, MDC.get(Span.TRACE_ID_NAME));
        message.getMessageProperties().setHeader(Span.SPAN_ID_NAME, MDC.get(Span.SPAN_ID_NAME));

        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
        message.getMessageProperties().setAppId(applicationName);

        if (message.getMessageProperties().getMessageId() == null)
            message.getMessageProperties().setMessageId(IdWorker.getIdStr());

        super.send(exchange, routingKey, message, correlationData);
    }


    @Override
    protected Message doReceiveNoWait(String queueName) {
        Message message = super.doReceiveNoWait(queueName);

        return message;
    }


    @Override
    public Message receive(final String queueName, final long timeoutMillis) {
        Message message = super.receive(queueName, timeoutMillis);

        return message;
    }

}
