package com.orderservice.util;

import com.orderservice.dtos.OrderServiceDTOPost;

public class CreatorOrderServiceDTOPost {

    public static OrderServiceDTOPost creatorOrderServiceDTOPost() {
        return OrderServiceDTOPost.builder()
                .productId(OrderServiceCreator.createOrderServiceToBeSaved().getProductId())
                .quantity(OrderServiceCreator.createOrderServiceToBeSaved().getQuantity())
                .build();
    }

}
