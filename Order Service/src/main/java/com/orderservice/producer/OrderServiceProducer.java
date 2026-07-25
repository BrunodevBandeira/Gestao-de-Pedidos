package com.orderservice.producer;

import com.orderservice.event.OrderCreatedEvent;
import com.orderservice.model.OrderServiceModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceProducer {

    private final RabbitTemplate rabbitTemplate;

    public OrderServiceProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${broker.exchange}")
    private String exchange;

    @Value("${broker.routingkey.orderCreated}")
    private String orderCreatedKey;

    public void publishOrderCreated(OrderServiceModel order) {
        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getOrderID(),
                order.getProductId(),
                order.getQuantity()
        );

        rabbitTemplate.convertAndSend(exchange, orderCreatedKey, event);
    }
}
