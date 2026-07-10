package com.orderservice.mapper;

import com.orderservice.dtos.OrderServiceDTOPut;
import com.orderservice.model.OrderServiceModel;
import org.mapstruct.Mapper;

// componentModel = "spring": a implementação gerada (OrderMapperImpl) vira um bean,
// então é só injetar OrderMapper no service — nada de INSTANCE
@Mapper(componentModel = "spring")
public interface OrderMapper {

    // ida: DTO que chegou na API -> entidade pra salvar no banco
    OrderServiceModel toModel(OrderServiceDTOPut dto);

    // volta: entidade salva -> DTO pra devolver na resposta
    OrderServiceDTOPut toDTO(OrderServiceModel model);
}
