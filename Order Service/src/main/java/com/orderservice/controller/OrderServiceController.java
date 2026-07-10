package com.orderservice.controller;


import com.orderservice.dtos.OrderServiceDTOPut;
import com.orderservice.repository.OrderServiceRepository;
import com.orderservice.service.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderServiceController {

    private final OrderServiceImpl orderService;

    public OrderServiceController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderServiceDTOPut> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<Page<OrderServiceDTOPut>> getAllOrder(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAllOrder(pageable));
    }

    @PostMapping
    public ResponseEntity<OrderServiceDTOPut> createOrder(@Valid @RequestBody OrderServiceDTOPut orderServiceDTO) {
        OrderServiceDTOPut created = orderService.createOrder(orderServiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrderServiceDTOPut> updateOrder(@PathVariable UUID id, @Valid @RequestBody OrderServiceDTOPut orderServiceDTO) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderServiceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
