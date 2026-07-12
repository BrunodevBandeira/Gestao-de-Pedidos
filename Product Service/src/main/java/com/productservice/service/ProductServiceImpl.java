package com.productservice.service;

import com.productservice.dtos.ProductRequest;
import com.productservice.dtos.ProductResponse;
import com.productservice.dtos.StockResponse;
import com.productservice.exceptions.BadRequestException;
import com.productservice.mapper.ProductMapper;
import com.productservice.model.ProductModel;
import com.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse getProductById(UUID id) {
        return productMapper.toDTO(findProductById(id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toDTO);
    }

    @Override
    public StockResponse isStock(UUID id) {
        ProductModel product = findProductById(id);
        return new StockResponse(product.getStockQuantity() > 0);
    }


    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        ProductModel productModel = productMapper.toModel(productRequest);
        ProductModel saved = productRepository.save(productModel);
        return productMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(UUID id, ProductRequest productRequest) {
        ProductModel productModel = findProductById(id);

        productModel.setName(productRequest.name());
        productModel.setPrice(productRequest.price());
        productModel.setStockQuantity(productRequest.stockQuantity());

        ProductModel saved = productRepository.save(productModel);

        return productMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.delete(findProductById(id));
    }


    private ProductModel findProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new BadRequestException("Product not found: " + id));
    }
}
