package com.orderservice.util;

import com.orderservice.model.OrderServiceModel;

import java.util.UUID;

public final class OrderServiceCreator {

    public static final UUID PRODUCT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private OrderServiceCreator() {
    }

    public static OrderServiceModel createOrderServiceToBeSaved() {
        return OrderServiceModel.builder()
                .productId(PRODUCT_ID)
                .quantity(2)
                .valueTotal(456.12)
                .status("Enviado")
                .build();
    }

    public static OrderServiceModel createValidOrderService() {
        return OrderServiceModel.builder()
                .orderID(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .productId(PRODUCT_ID)
                .quantity(2)
                .valueTotal(456.12)
                .status("Enviado")
                .build();
    }

    public static OrderServiceModel updateValidOrderService() {
        return OrderServiceModel.builder()
                .orderID(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .productId(PRODUCT_ID)
                .quantity(2)
                .valueTotal(123.45)
                .status("Entregue")
                .build();
    }
}
