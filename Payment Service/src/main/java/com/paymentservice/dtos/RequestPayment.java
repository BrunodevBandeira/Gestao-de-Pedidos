package com.paymentservice.dtos;

import java.util.UUID;

public record RequestPayment(
        UUID orderId,
        Double amount
) {
}
