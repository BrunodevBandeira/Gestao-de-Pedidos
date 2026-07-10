package com.orderservice.repository;

import com.orderservice.model.OrderServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
 import java.util.UUID;

public interface OrderServiceRepository extends JpaRepository<OrderServiceModel, UUID> {
}
