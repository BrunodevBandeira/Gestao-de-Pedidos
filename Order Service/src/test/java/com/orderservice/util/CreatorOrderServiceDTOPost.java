package com.orderservice.util;

import com.orderservice.dtos.OrderServiceDTOPost;

import java.time.LocalDateTime;

public class CreatorOrderServiceDTOPost {

    public static OrderServiceDTOPost creatorOrderServiceDTOPost() {
        return OrderServiceDTOPost.builder()
                .status(OrderServiceCreator.createOrderServiceToBeSaved().getStatus())
                .valueTotal(OrderServiceCreator.createOrderServiceToBeSaved().getValueTotal())
                .date(OrderServiceCreator.createOrderServiceToBeSaved().getDate())
                .build();
    }


}
