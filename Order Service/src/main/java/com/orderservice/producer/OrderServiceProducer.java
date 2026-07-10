package com.orderservice.producer;

import com.orderservice.controller.OrderServiceController;
import com.orderservice.dtos.OrderServiceDTOPut;
import com.orderservice.model.OrderServiceModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceProducer {

    final RabbitTemplate rabbitTemplate;

    public OrderServiceProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.direct}")
    private String directQueue;

    @Value(value = "${broker.exchange.direct}")
    private String directExchange;

    @Value(value = "${broker.routingkey.direct}")
    private String directRoutingkey;

    public void directPublishMsg(OrderServiceModel orderServiceModel) {
        OrderServiceDTOPut orderServiceDTOPut = OrderServiceDTOPut.builder()
                .orderID(orderServiceModel.getOrderID())
                .status(orderServiceModel.getStatus())
                .valueTotal(orderServiceModel.getValueTotal())
                .date(orderServiceModel.getDate())
                .build();

        rabbitTemplate.convertAndSend(directExchange, directRoutingkey, orderServiceDTOPut);

    }


}
