package com.productservice.config;

import com.productservice.model.ProductModel;
import com.productservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class DataSeeder {

    private static final UUID ESPRESSO_SIMPLES = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID ESPRESSO_DUPLO   = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID RISTRETTO        = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID LUNGO            = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Bean
    CommandLineRunner seedProducts(ProductRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }
            repository.saveAll(List.of(
                    ProductModel.builder().productId(ESPRESSO_SIMPLES).name("Espresso Simples").price(7.00).stockQuantity(50).build(),
                    ProductModel.builder().productId(ESPRESSO_DUPLO).name("Espresso Duplo").price(9.00).stockQuantity(50).build(),
                    ProductModel.builder().productId(RISTRETTO).name("Ristretto").price(8.00).stockQuantity(50).build(),
                    ProductModel.builder().productId(LUNGO).name("Lungo").price(8.00).stockQuantity(50).build()
            ));
        };
    }
}
