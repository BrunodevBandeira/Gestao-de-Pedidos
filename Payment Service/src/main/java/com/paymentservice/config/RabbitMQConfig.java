package com.paymentservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitMQConfig {

    @Value("${broker.exchange}")
    private String exchange;

    @Value("${broker.queue.payment}")
    private String paymentQueue;

    @Value("${broker.routingkey.stockReserved}")
    private String stockReservedKey;

    @Bean
    public DirectExchange ordersExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(paymentQueue, true);
    }

    @Bean
    public Binding paymentBinding(Queue paymentQueue, DirectExchange ordersExchange) {
        return BindingBuilder.bind(paymentQueue).to(ordersExchange).with(stockReservedKey);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        JsonMapper jsonMapper = new JsonMapper();
        return new JacksonJsonMessageConverter(jsonMapper);
    }
}
