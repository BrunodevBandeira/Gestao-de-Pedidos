package com.orderservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderServiceDTOPut(
          UUID orderID,
          @NotNull UUID productId,
          @NotNull @Positive Integer quantity,
          String productName,
          @NotBlank String status,
          @NotNull @Positive Double valueTotal,
          LocalDateTime date
) {}
