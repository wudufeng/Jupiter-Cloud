package com.jupiterframework.amqp.core;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import com.jupiterframework.transaction.TransactionDetermine;


/**
 * 判断是否在数据库事务里
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
    public void send(String exchange, String routingKey, Message message, CorrelationData correlationData)
            throws AmqpException {
        // 判断是否在数据库事务里
        if (transactionDetermine.isActualTransactionActive()) {
            // 拦截消息发送，在事务提交后才发送
            MessageThreadLocalContext
                .registryProducerMessage(new MessageData(exchange, routingKey, message, correlationData));
            return;
        }

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
