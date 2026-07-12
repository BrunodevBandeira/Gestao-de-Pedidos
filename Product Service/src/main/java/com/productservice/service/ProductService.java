package com.productservice.service;

import com.productservice.dtos.ProductRequest;
import com.productservice.dtos.ProductResponse;
import com.productservice.dtos.StockResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponse getProductById(UUID id);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    StockResponse isStock(UUID id);

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct(UUID id, ProductRequest productRequest);

    void deleteProduct(UUID id);
}
