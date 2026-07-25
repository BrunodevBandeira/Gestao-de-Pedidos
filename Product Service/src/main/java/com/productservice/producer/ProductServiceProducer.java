package com.productservice.producer;

import com.productservice.event.StockReservedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductServiceProducer {

    private final RabbitTemplate rabbitTemplate;

    public ProductServiceProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${broker.exchange}")
    private String exchange;

    @Value("${broker.routingkey.stockReserved}")
    private String stockReservedKey;

    public void publishStockReserved(UUID orderId, boolean reserved, String productName, Double valueTotal) {
        StockReservedEvent event = new StockReservedEvent(orderId, reserved, productName, valueTotal);

        rabbitTemplate.convertAndSend(exchange, stockReservedKey, event);
    }
}
