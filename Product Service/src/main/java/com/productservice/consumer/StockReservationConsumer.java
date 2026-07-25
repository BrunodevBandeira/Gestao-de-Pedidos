package com.productservice.consumer;

import com.productservice.event.OrderCreatedEvent;
import com.productservice.producer.ProductServiceProducer;
import com.productservice.service.ProductService;
import com.productservice.service.ReservationResult;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class StockReservationConsumer {

    private final ProductService productService;
    private final ProductServiceProducer productProducer;

    public StockReservationConsumer(ProductService productService, ProductServiceProducer productProducer) {
        this.productService = productService;
        this.productProducer = productProducer;
    }

    @RabbitListener(queues = "${broker.queue.stockReservation}")
    public void onOrderCreated(OrderCreatedEvent event) {
        System.out.println("[PRODUCT] Recebi order.created do pedido " + event.orderId()
                + " -> produto " + event.productId() + " x" + event.quantity());

        ReservationResult result = productService.reserveStock(event.productId(), event.quantity());
        System.out.println("[PRODUCT] Reserva do pedido " + event.orderId() + ": " + result.reserved()
                + " | " + result.productName() + " | total=" + result.valueTotal());

        productProducer.publishStockReserved(
                event.orderId(), result.reserved(), result.productName(), result.valueTotal());
        System.out.println("[PRODUCT] Publiquei stock.reserved do pedido " + event.orderId());
    }
}
