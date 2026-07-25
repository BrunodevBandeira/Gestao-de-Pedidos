package com.productservice.event;

import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,
        UUID productId,
        Integer quantity
) {}
