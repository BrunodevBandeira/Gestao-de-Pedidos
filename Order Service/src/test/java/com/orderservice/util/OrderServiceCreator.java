package com.orderservice.util;

import com.orderservice.model.OrderServiceModel;

import java.util.UUID;

public final class OrderServiceCreator {

    private OrderServiceCreator() {
    }

    // SEM id: o pedido antes de ser salvo (o que chega na requisição)
    public static OrderServiceModel createOrderServiceToBeSaved() {
        return OrderServiceModel.builder()
                .valueTotal(456.12)
                .status("Enviado")
                .build();                 // sem date: @CreationTimestamp preenche no insert
    }

    // COM id: o pedido já salvo (o que o repositório devolve)
    public static OrderServiceModel createValidOrderService() {
        return OrderServiceModel.builder()
                .orderID(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .valueTotal(456.12)
                .status("Enviado")
                .build();
    }

    // MESMO id, valores atualizados: o pedido depois de um update
    public static OrderServiceModel updateValidOrderService() {
        return OrderServiceModel.builder()
                .orderID(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .valueTotal(123.45)
                .status("Entregue")
                .build();
    }
}
