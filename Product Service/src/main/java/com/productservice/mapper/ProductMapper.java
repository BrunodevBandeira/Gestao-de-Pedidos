package com.productservice.mapper;

import com.productservice.dtos.ProductRequest;
import com.productservice.dtos.ProductResponse;
import com.productservice.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    /**
     * Converte os dados recebidos na requisição (DTO)
     * para a entidade que será persistida no banco.
     *
     * Fluxo:
     * ProductRequest -> ProductModel
     */
    ProductModel toModel(ProductRequest request);

    /**
     * Converte a entidade recuperada do banco
     * para o DTO de resposta enviado ao cliente.
     *
     * Fluxo:
     * ProductModel -> ProductResponse
     */
    ProductResponse toDTO(ProductModel model);

    /**
     * Atualiza uma entidade já existente com os dados
     * recebidos na requisição.
     *
     * O @MappingTarget indica ao MapStruct que ele deve
     * modificar o objeto existente, em vez de criar um novo.
     **
     *
     * Fluxo:
     * ProductRequest + ProductModel existente -> ProductModel atualizado
     */
    void updateModelFromRequest(ProductRequest request,
                                @MappingTarget ProductModel model);
}





















