package com.jupiterframework.amqp.core;

import java.util.ArrayList;
import java.util.List;


/**
 * 发送MQ消息，如果当前开启了事务，则将消息存放在此容器里，提交事务后才发送消息
 * 
 * @author wudf
 *
 */
public class MessageThreadLocalContext {
    private MessageThreadLocalContext() {
    }

    /** 消息生产者存放的上下文 */
    private static final ThreadLocal<List<MessageData>> MESSAGE_PRODUCER_HOLDER = ThreadLocal.withInitial(ArrayList::new);

    /** 消息消费者存放的上下文 */
    private static final ThreadLocal<Long> MESSAGE_CONSUME_HOLDER = new ThreadLocal<>();


    /** 生产者存储一个消息 */
    public static void registryProducerMessage(MessageData message) {
        MESSAGE_PRODUCER_HOLDER.get().add(message);
    }


    /** 事务提交成功后，从上下文获取一个消息 */
    public static List<MessageData> takeProducerMessage() {
        return MESSAGE_PRODUCER_HOLDER.get();
    }


    public static void registryConsumerMessage(Long messageId) {
        MESSAGE_CONSUME_HOLDER.set(messageId);
    }


    public static Long takeConsumerMessage() {
        return MESSAGE_CONSUME_HOLDER.get();
    }


    public static void clear() {
        MESSAGE_PRODUCER_HOLDER.get().clear();
        MESSAGE_CONSUME_HOLDER.set(null);
    }
}
