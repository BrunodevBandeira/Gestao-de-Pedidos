package com.paymentservice.consumer;

import com.paymentservice.event.StockReservedEvent;
import com.paymentservice.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {

    private final PaymentService paymentService;

    public PaymentConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = "${broker.queue.payment}")
    public void onStockReserved(StockReservedEvent event) {
        System.out.println("[PAYMENT] Recebi stock.reserved do pedido " + event.orderId()
                + " | reservado? " + event.reserved() + " | total=" + event.valueTotal());

        paymentService.processStockReserved(event);
    }
}
