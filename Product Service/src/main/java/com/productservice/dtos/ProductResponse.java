package com.productservice.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductResponse(
        UUID productId,
        String name,
        double price,
        int stockQuantity
) {}
