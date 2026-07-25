package com.orderservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderServiceDTOPost(
        @NotNull UUID productId,
        @NotNull @Positive Integer quantity
){}
