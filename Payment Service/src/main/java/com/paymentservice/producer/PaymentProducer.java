package com.paymentservice.producer;

import com.paymentservice.event.PaymentProcessedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentProducer {

    private final RabbitTemplate rabbitTemplate;

    public PaymentProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${broker.exchange}")
    private String exchange;

    @Value("${broker.routingkey.paymentApproved}")
    private String paymentApprovedKey;

    @Value("${broker.routingkey.paymentFailed}")
    private String paymentFailedKey;

    public void publishPaymentResult(UUID orderId, boolean approved, Double amount, String transactionId) {
        PaymentProcessedEvent event = new PaymentProcessedEvent(orderId, approved, amount, transactionId);

        String routingKey = approved ? paymentApprovedKey : paymentFailedKey;

        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
