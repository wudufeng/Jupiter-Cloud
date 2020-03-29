package com.jupiterframework.amqp.core;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.MDC;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RetryRequeueRecoverer implements MessageRecoverer {

    private org.springframework.amqp.core.AmqpTemplate amqpTemplate;

    @Value("${mq.retry.exchange:common_retry}")
    private String exchange;

    @Value("${mq.retry.routingKey:common_retry}")
    private String routingKey;

    @Value("${spring.rabbitmq.virtualHost:}")
    private String virtualHost;
    @Value("${spring.application.name:unknow}")
    private String applicationName;

    public RetryRequeueRecoverer(AmqpTemplate amqpTemplate) {
        super();
        this.amqpTemplate = amqpTemplate;
    }


    @Override
    public void recover(Message message, Throwable cause) {
        if (log.isWarnEnabled()) {
            log.warn("Retries exhausted for message {}", new String(message.getBody(), StandardCharsets.UTF_8), cause);
        }

        MessageProperties mp = new MessageProperties();
        mp.setHeader("RetryMessage", "TRUE");

        JSONObject ob = null;
        if (message.getMessageProperties().getHeaders().get("RetryMessage") != null) {
            mp.setMessageId(message.getMessageProperties().getMessageId());
            amqpTemplate.send(exchange, routingKey, new Message(message.getBody(), mp));

        } else {

            ob = (JSONObject) JSON.parse("{}");
            String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
            ob.put("messageBody", messageBody);
            ob.put("applicationName", applicationName);// 处理失败的应用
            ob.put("virtualHost", virtualHost);
            ob.put("exchange", message.getMessageProperties().getReceivedExchange());
            ob.put("routingKey", message.getMessageProperties().getReceivedRoutingKey());
            ob.put("consumerQueue", message.getMessageProperties().getConsumerQueue());
            ob.put("messageId", DigestUtils.md5Hex(messageBody));
            ob.put("messageTime", new Date());// 处理失败的时间
            ob.put("exception", ExceptionUtils.getRootCauseMessage(cause));// 处理失败的时间

            try {
                ob.put("traceId", MDC.get(Span.TRACE_ID_NAME)); // 链路跟踪号
                ob.put("spanId", MDC.get(Span.SPAN_ID_NAME)); // 执行当前消息跟踪号
                // ob.put("ip", NetUtil.getLocalHost());
            } catch (Exception e) {
                log.debug("", e);
            }
            amqpTemplate.send(exchange, routingKey, new Message(JSON.toJSONBytes(ob), mp));
        }

    }

}
