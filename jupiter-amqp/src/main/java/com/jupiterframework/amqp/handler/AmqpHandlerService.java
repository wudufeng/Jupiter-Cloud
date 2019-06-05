package com.jupiterframework.amqp.handler;

import org.springframework.amqp.core.Message;
import org.springframework.cloud.sleuth.Span;


/**
 * 消息拦截处理
 * 
 * @author hesp
 *
 */
public interface AmqpHandlerService {

    /**
     * 持久化消息
     * 
     * @param span
     * @param message
     * @return
     */
    public Long insertMessage(Span span, Message message);


    /**
     * 消费失败
     * 
     * @param messageId
     * @param e
     */
    public int consumeFail(Long messageId, Exception e);


    /**
     * 消费成功
     * 
     * @param messageId
     */
    public void consumeSucc(Long messageId);
}
