package com.orderservice.controller;

import com.orderservice.dtos.OrderServiceDTOPost;
import com.orderservice.dtos.OrderServiceDTOPut;
import com.orderservice.service.OrderServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
 import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@DisplayName("Testes do OrderServiceControllerTest")
@WebMvcTest(OrderServiceController.class)
class OrderServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("GET /order/{id} devolve 200 e o pedido quando existe")
    void getOrderById_ReturnsOrder_WhenSuccessful() throws Exception {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
        OrderServiceDTOPut dtoDeVolta = OrderServiceDTOPut.builder()
                .orderID(id)
                .status("Enviado")
                .valueTotal(456.12)
                .date(LocalDateTime.now())
                .build();

        given(orderService.getOrderById(id)).willReturn(dtoDeVolta);

        mockMvc.perform(get("/order/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Enviado"))
                .andExpect(jsonPath("$.valueTotal").value(456.12));
    }

    @Test
    @DisplayName("POST /order cria o pedido e devolve 201")
    void createOrder_PersistsAndReturns201_WhenSuccessful() throws Exception {
        OrderServiceDTOPost paraEnviar = OrderServiceDTOPost.builder()
                .productId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .quantity(2)
                .build();

        OrderServiceDTOPut criado = OrderServiceDTOPut.builder()
                .orderID(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .productId(UUID.fromString("22222222-2222-2222-2222-222222222222"))
                .quantity(2)
                .status("PENDING")
                .date(LocalDateTime.now())
                .build();

        given(orderService.createOrder(any(OrderServiceDTOPost.class))).willReturn(criado);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paraEnviar)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderID").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService, times(1)).createOrder(any(OrderServiceDTOPost.class));
    }

    @Test
    @DisplayName("PUT /order/{id} atualiza e devolve 200")
    void updateOrder_UpdatesAndReturns200_WhenSuccessful() throws Exception {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");

        OrderServiceDTOPut paraEnviar = OrderServiceDTOPut.builder()
                .status("Entregue")
                .valueTotal(999.99)
                .build();

        OrderServiceDTOPut atualizado = OrderServiceDTOPut.builder()
                .orderID(id)
                .status("Entregue")
                .valueTotal(999.99)
                .date(LocalDateTime.now())
                .build();

        given(orderService.updateOrder(eq(id), any(OrderServiceDTOPut.class)))
                .willReturn(atualizado);

        mockMvc.perform(put("/order/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paraEnviar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Entregue"))
                .andExpect(jsonPath("$.valueTotal").value(999.99));

        verify(orderService, times(1)).updateOrder(eq(id), any(OrderServiceDTOPut.class));
    }

    @Test
    @DisplayName("DELETE /order/{id} devolve 204 e chama o service")
    void deleteOrder_Removes_WhenSuccessful() throws Exception {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");

        willDoNothing().given(orderService).deleteOrder(id);

        mockMvc.perform(delete("/order/{id}", id))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(id);
    }

    @Test
    @DisplayName("GET-PAGE /order devolve 200 e uma página de pedidos")
    void getAllOrder_ReturnsPage_WhenSuccessful() throws Exception {
        OrderServiceDTOPut dto = OrderServiceDTOPut.builder()
                .orderID(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .status("Enviado")
                .valueTotal(456.12)
                .date(LocalDateTime.now())
                .build();

        Page<OrderServiceDTOPut> pagina = new PageImpl<>(List.of(dto));

        given(orderService.getAllOrder(any(Pageable.class))).willReturn(pagina);

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].status").value("Enviado"))
                .andExpect(jsonPath("$.content[0].valueTotal").value(456.12));
    }
}

