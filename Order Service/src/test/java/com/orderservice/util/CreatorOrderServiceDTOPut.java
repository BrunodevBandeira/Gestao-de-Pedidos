package com.orderservice.util;

import com.orderservice.dtos.OrderServiceDTOPut;

import java.time.LocalDateTime;

public class CreatorOrderServiceDTOPut {

    public static OrderServiceDTOPut orderServiceDTOPut() {
        return OrderServiceDTOPut.builder()
                .orderID(OrderServiceCreator.createOrderServiceToBeSaved().getOrderID())
                .valueTotal(OrderServiceCreator.createOrderServiceToBeSaved().getValueTotal())
                .status(OrderServiceCreator.createValidOrderService().getStatus())
                .date(OrderServiceCreator.createValidOrderService().getDate())
                .build();
    }
}
