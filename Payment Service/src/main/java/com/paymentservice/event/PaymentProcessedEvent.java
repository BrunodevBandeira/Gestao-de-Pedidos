package com.paymentservice.event;

import java.util.UUID;

public record PaymentProcessedEvent(
        UUID orderId,
        boolean approved,
        Double amount,
        String transactionId
) {
}
