package com.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_order_service")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderServiceModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderID;

    private UUID productId;

    private Integer quantity;

    private String productName;

    private String status;

    private Double valueTotal;

    @CreationTimestamp
    private LocalDateTime date;
}

