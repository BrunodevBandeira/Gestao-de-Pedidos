package com.orderservice.event;

import java.util.UUID;

public record StockReservedEvent(
        UUID orderId,
        boolean reserved,
        String productName,
        Double valueTotal
) {}
