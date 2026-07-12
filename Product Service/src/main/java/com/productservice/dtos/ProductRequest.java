package com.productservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record ProductRequest(
        @NotBlank String name,
        @NotNull @Positive Double price,
        @NotNull @PositiveOrZero Integer stockQuantity
) {}
