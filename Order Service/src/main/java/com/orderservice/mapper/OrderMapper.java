package com.orderservice.mapper;

import com.orderservice.dtos.OrderServiceDTOPost;
import com.orderservice.dtos.OrderServiceDTOPut;
import com.orderservice.model.OrderServiceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderID", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "valueTotal", ignore = true)
    @Mapping(target = "productName", ignore = true)
    OrderServiceModel toModel(OrderServiceDTOPost dto);

    OrderServiceDTOPut toDTO(OrderServiceModel model);
}
