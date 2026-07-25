package com.productservice.mapper;

import com.productservice.dtos.ProductRequest;
import com.productservice.dtos.ProductResponse;
import com.productservice.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductModel toModel(ProductRequest request);

    ProductResponse toDTO(ProductModel model);

    void updateModelFromRequest(ProductRequest request,
                                @MappingTarget ProductModel model);
}

