package com.orderservice.service;

import com.orderservice.dtos.OrderServiceDTOPost;
import com.orderservice.dtos.OrderServiceDTOPut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderServiceService {

    OrderServiceDTOPut getOrderById(UUID id);
    Page<OrderServiceDTOPut> getAllOrder(Pageable pageable);

    OrderServiceDTOPut createOrder(OrderServiceDTOPost orderServiceDTO);

    OrderServiceDTOPut updateOrder(UUID id, OrderServiceDTOPut orderServiceDTO);

    void deleteOrder(UUID id);
}
