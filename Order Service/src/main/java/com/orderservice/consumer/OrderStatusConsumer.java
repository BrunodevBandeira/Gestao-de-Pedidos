package com.orderservice.consumer;

import com.orderservice.event.StockReservedEvent;
import com.orderservice.service.OrderServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusConsumer {

    private final OrderServiceImpl orderService;

    public OrderStatusConsumer(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = "${broker.queue.orderStatus}")
    public void onStockReserved(StockReservedEvent event) {
        System.out.println("[ORDER] Recebi stock.reserved do pedido " + event.orderId()
                + " -> reservado? " + event.reserved()
                + " | " + event.productName() + " | total=" + event.valueTotal());
        orderService.applyStockResult(
                event.orderId(), event.reserved(), event.productName(), event.valueTotal());
    }
}
