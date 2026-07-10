package com.orderservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record OrderServiceDTOPost(
        @NotBlank String status,
        @NotNull @Positive Double valueTotal,
        LocalDateTime date
){}
