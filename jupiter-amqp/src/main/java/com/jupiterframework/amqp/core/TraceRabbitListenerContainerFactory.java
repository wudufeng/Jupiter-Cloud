package com.jupiterframework.amqp.core;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;


public class TraceRabbitListenerContainerFactory extends SimpleRabbitListenerContainerFactory {

    @Override
    protected SimpleMessageListenerContainer createContainerInstance() {
        TraceMessageListenerContainer container = new TraceMessageListenerContainer();
        container.setErrorHandler(new ErrorMonitorHandler());
        return container;
    }

    public static class ErrorMonitorHandler extends ConditionalRejectingErrorHandler {
        @Override
        public void handleError(Throwable t) {
            super.handleError(t);
        }
    }
}
