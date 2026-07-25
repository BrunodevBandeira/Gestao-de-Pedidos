package com.productservice.service;

public record ReservationResult(
        boolean reserved,
        String productName,
        Double valueTotal
) {}
