package com.productservice.config;

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
public class RabbitConfig {

    @Value("${broker.exchange}")
    private String exchange;

    @Value("${broker.queue.stockReservation}")
    private String stockReservationQueue;

    @Value("${broker.routingkey.orderCreated}")
    private String orderCreatedKey;

    @Bean
    public DirectExchange ordersExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue stockReservationQueue() {
        return new Queue(stockReservationQueue, true);
    }

    @Bean
    public Binding stockReservationBinding(Queue stockReservationQueue, DirectExchange ordersExchange) {
        return BindingBuilder.bind(stockReservationQueue).to(ordersExchange).with(orderCreatedKey);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        JsonMapper jsonMapper = new JsonMapper();
        return new JacksonJsonMessageConverter(jsonMapper);
    }
}
