package com.orderservice.service;

import com.orderservice.dtos.OrderServiceDTOPut;
import com.orderservice.exceptions.BadRequestException;
import com.orderservice.mapper.OrderMapper;
import com.orderservice.model.OrderServiceModel;
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

    public OrderServiceImpl(OrderServiceRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
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
    public OrderServiceDTOPut createOrder(OrderServiceDTOPut orderServiceDTO) {

        // ida: DTO -> entidade
        OrderServiceModel model = orderMapper.toModel(orderServiceDTO);

        OrderServiceModel saved = orderRepository.save(model);

        // volta: entidade salva (agora com orderID gerado pelo banco) -> DTO
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
}
