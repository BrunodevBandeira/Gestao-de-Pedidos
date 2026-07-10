package com.orderservice.repository;

import com.orderservice.model.OrderServiceModel;
import com.orderservice.util.OrderServiceCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest // ← sobe SÓ a camada de persistência + banco H2 em
@DisplayName("Testes do repositório de Order Service")
class OrderServiceRepositoryTest {

    @Autowired
    private OrderServiceRepository repository; // ← injeta o repositório de verdade (não é mock!)



    @Test // @Test roda numa transação que dá ROLLBACK no fim  → um teste não "suja" o banco pro outro
    @DisplayName("save persiste um pedido e gera o id")
    void save_PersistsOrderService_WhenSuccessful() {
        // Arrange: um pedido ainda SEM id
        OrderServiceModel toBeSaved = OrderServiceCreator.createOrderServiceToBeSaved();

        // Act: salvar E mandar o INSERT pro banco na hora (flush),
        // pra o @CreationTimestamp gerar a data antes das asserções
        OrderServiceModel saved = repository.saveAndFlush(toBeSaved);

        // Assert
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getOrderID()).isNotNull();   // o banco gerou o UUID
        Assertions.assertThat(saved.getDate()).isNotNull();      // @CreationTimestamp preencheu
        Assertions.assertThat(saved.getStatus()).isEqualTo(toBeSaved.getStatus());
    }

    @Test
    @DisplayName("update altera um pedido existente mantendo o mesmo id")
    void update_UpdatesOrderService_WhenSuccessful() {
        OrderServiceModel saved = repository.save(OrderServiceCreator.createOrderServiceToBeSaved());

        saved.setStatus("Entregue");
        saved.setValueTotal(999.99);
        OrderServiceModel updated = repository.save(saved);

        Assertions.assertThat(updated).isNotNull();
        Assertions.assertThat(updated.getOrderID()).isEqualTo(saved.getOrderID()); // mesmo id
        Assertions.assertThat(updated.getStatus()).isEqualTo("Entregue");
        Assertions.assertThat(updated.getValueTotal()).isEqualTo(999.99);
    }

    @Test
    @DisplayName("delete remove um pedido quando bem-sucedido")
    void delete_RemovesOrderService_WhenSuccessful() {
        OrderServiceModel saved = repository.save(OrderServiceCreator.createOrderServiceToBeSaved());

        repository.delete(saved);

        Optional<OrderServiceModel> found = repository.findById(saved.getOrderID());
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findById devolve o pedido quando ele existe")
    void findById_ReturnsOrderService_WhenSuccessful() {
        OrderServiceModel saved = repository.save(OrderServiceCreator.createOrderServiceToBeSaved());

        Optional<OrderServiceModel> found = repository.findById(saved.getOrderID());

        Assertions.assertThat(found).isPresent().contains(saved);
    }
}
