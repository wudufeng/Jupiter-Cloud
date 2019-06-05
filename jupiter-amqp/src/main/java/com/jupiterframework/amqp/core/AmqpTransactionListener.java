package com.jupiterframework.amqp.core;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.jupiterframework.amqp.handler.AmqpHandlerService;
import com.jupiterframework.transaction.TransactionActionListener;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AmqpTransactionListener implements TransactionActionListener {

    private RabbitTemplate amqpTemplate;
    private AmqpHandlerService amqpHandlerService;


    public AmqpTransactionListener(RabbitTemplate amqpTemplate, AmqpHandlerService amqpHandler) {
        this.amqpTemplate = amqpTemplate;
        this.amqpHandlerService = amqpHandler;
    }


    @Override
    public void doBeforeCommit() {
        amqpHandlerService.consumeSucc(MessageThreadLocalContext.takeConsumerMessage());
    }


    @Override
    public void doAfterCommit() {

        // 事务提交成功后才发送消息
        List<MessageData> messageList = MessageThreadLocalContext.takeProducerMessage();
        while (!messageList.isEmpty()) {
            MessageData m = messageList.remove(0);
            try {
                amqpTemplate.send(m.getExchange(), m.getRoutingKey(), m.getMessage(), m.getCorrelationData());
            } catch (Exception e) {
                log.error("send message fail ! , {} ", m, e);
            }

        }
    }


    @Override
    public void doBeforeRollback() {
        MessageThreadLocalContext.clear();
    }


    @Override
    public void doCleanupAfterCompletion() {
        MessageThreadLocalContext.clear();// rollback 需要清理消息
    }
}
