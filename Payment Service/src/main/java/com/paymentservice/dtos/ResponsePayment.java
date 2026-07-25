package com.paymentservice.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResponsePayment(
        UUID paymentId,
        UUID orderId,
        String productName,
        Double amount,
        String status,
        String transactionId,
        LocalDateTime processedAt
) {
}
