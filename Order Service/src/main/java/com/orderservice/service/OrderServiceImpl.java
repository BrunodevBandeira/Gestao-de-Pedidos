package com.orderservice.service;

import com.orderservice.dtos.OrderServiceDTOPost;
import com.orderservice.dtos.OrderServiceDTOPut;
import com.orderservice.exceptions.BadRequestException;
import com.orderservice.mapper.OrderMapper;
import com.orderservice.model.OrderServiceModel;
import com.orderservice.producer.OrderServiceProducer;
import com.orderservice.repository.OrderServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderServiceService {

    private final OrderServiceRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderServiceProducer orderProducer;

    public OrderServiceImpl(OrderServiceRepository orderRepository, OrderMapper orderMapper,
                            OrderServiceProducer orderProducer) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderProducer = orderProducer;
    }

    @Override
    public OrderServiceDTOPut getOrderById(UUID id) {
        return orderMapper.toDTO(findOrderById(id));
    }

    @Override
    public Page<OrderServiceDTOPut> getAllOrder(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDTO);
    }

    @Transactional
    @Override
    public OrderServiceDTOPut createOrder(OrderServiceDTOPost orderServiceDTO) {

        OrderServiceModel model = orderMapper.toModel(orderServiceDTO);
        model.setStatus("PENDING");

        OrderServiceModel saved = orderRepository.save(model);

        orderProducer.publishOrderCreated(saved);

        return orderMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public OrderServiceDTOPut updateOrder(UUID id, OrderServiceDTOPut orderServiceDTO) {
        OrderServiceModel model = findOrderById(id);

        model.setStatus(orderServiceDTO.status());
        model.setValueTotal(orderServiceDTO.valueTotal());

        OrderServiceModel saved = orderRepository.save(model);
        return orderMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public void deleteOrder(UUID id) {

        orderRepository.delete(findOrderById(id));
    }

    private OrderServiceModel findOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Order not found: " + id));
    }

    @Transactional
    public void applyStockResult(UUID orderId, boolean reserved, String productName, Double valueTotal) {
        OrderServiceModel order = findOrderById(orderId);
        order.setStatus(reserved ? "CONFIRMED" : "CANCELLED");
        if (reserved) {
            order.setProductName(productName);
            order.setValueTotal(valueTotal);
        }
        orderRepository.save(order);
    }
}
