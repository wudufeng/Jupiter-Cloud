package com.jupiterframework.amqp.core;

import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import com.jupiterframework.amqp.handler.AmqpHandlerService;
import com.rabbitmq.client.Channel;


/**
 * 接收消息，支持traceId,支持消费事务
 * 
 * @author wudf
 *
 */
public class TraceMessageListenerContainer extends SimpleMessageListenerContainer {

	private static final String SPAN_PREFIX = "amqp_";

	@Override
	protected void executeListener(Channel channel, Message messageIn) throws Throwable {
		logger.debug(messageIn);
		Span span = null;
		try {
			span = this.begin(messageIn);
			Long id = this.getAmqpHandler().insertMessage(span, messageIn);

			// 持久化消息
			if (id == 0) {// 无效消息/消息已存在,退出

				return;
			}

			this.invoke(span, id, messageIn);
		} catch (Exception e) {
			logger.error(messageIn, e);
			throw e;
		} finally {
			if (span != null)
				this.close(span);
		}
	}

	private AmqpHandlerService getAmqpHandler() {
		return getApplicationContext().getBean(AmqpHandlerService.class);
	}

	private void invoke(Span span, Long id, Message messageIn) throws Throwable {
		Span currentSpan = null;

		if (span == null)
			currentSpan = this.begin(messageIn);
		else
			getApplicationContext().getBean(Tracer.class).continueSpan(span);

		try {

			// 将消息ID存放在threadLocal里，事务提交前更新状态
			MessageThreadLocalContext.registryConsumerMessage(id);

			// 业务处理
			super.executeListener(null, messageIn);

			// 消费成功，如果当前无事务，则在此处更新
			this.getAmqpHandler().consumeSucc(id);
		} catch (Exception e) {
			// 消费失败
			this.getAmqpHandler().consumeFail(id, e);
		} finally {
			if (currentSpan != null) {
				this.close(span);
			}
		}
	}

	private Span begin(Message message) {
		Map<String, Object> headers = message.getMessageProperties().getHeaders();

		if (headers.containsKey(Span.TRACE_ID_NAME) && headers.containsKey(Span.SPAN_ID_NAME)) {
			Long traceId = Span.hexToId(String.valueOf(headers.get(Span.TRACE_ID_NAME)));
			Long spanId = Span.hexToId(String.valueOf(headers.get(Span.SPAN_ID_NAME)));
			Span parent = Span.builder().name(SPAN_PREFIX).traceId(traceId).spanId(spanId).build();
			return getApplicationContext().getBean(Tracer.class).createSpan(SPAN_PREFIX, parent);
		}
		return getApplicationContext().getBean(Tracer.class).createSpan(SPAN_PREFIX);
	}

	private void close(Span span) {
		getApplicationContext().getBean(Tracer.class).close(span);
	}

}
