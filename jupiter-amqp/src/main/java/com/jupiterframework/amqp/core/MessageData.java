package com.jupiterframework.amqp.core;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.support.CorrelationData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 消息参数对象
 * 
 * @author wudf
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageData {
    private String exchange;
    private String routingKey;
    private Message message;
    private CorrelationData correlationData;
}
