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
    private MockMvc mockMvc;  // simula requisições HTTP, sem subir um servidor real

    @Autowired
    private ObjectMapper objectMapper;     // converte objeto Java <-> JSON

    @MockitoBean
    private OrderServiceImpl orderService; // o DUBLÊ do service (o controller depende dele)

    @Test
    @DisplayName("GET /order/{id} devolve 200 e o pedido quando existe")
    void getOrderById_ReturnsOrder_WhenSuccessful() throws Exception {
        // ---- given (dado): programo o dublê ----
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");
        OrderServiceDTOPut dtoDeVolta = OrderServiceDTOPut.builder()
                .orderID(id)
                .status("Enviado")
                .valueTotal(456.12)
                .date(LocalDateTime.now())
                .build();

        given(orderService.getOrderById(id)).willReturn(dtoDeVolta);

        // ---- when + then (quando + então): disparo o HTTP e verifico ----
        mockMvc.perform(get("/order/{id}", id))
                .andExpect(status().isOk())                          // status 200
                .andExpect(jsonPath("$.status").value("Enviado"))    // corpo JSON: campo "status"
                .andExpect(jsonPath("$.valueTotal").value(456.12));  // campo "valueTotal"
    }


    @Test
    @DisplayName("POST /order cria o pedido e devolve 201")
    void createOrder_PersistsAndReturns201_WhenSuccessful() throws Exception {
        // ---- given ----
        // o que o CLIENTE envia (sem id, sem date — o servidor é quem gera)
        OrderServiceDTOPut paraEnviar = OrderServiceDTOPut.builder()
                .status("Enviado")
                .valueTotal(456.12)
                .build();

        // o que o service (dublê) devolve: já "criado", com id
        OrderServiceDTOPut criado = OrderServiceDTOPut.builder()
                .orderID(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .status("Enviado")
                .valueTotal(456.12)
                .date(LocalDateTime.now())
                .build();

        given(orderService.createOrder(any(OrderServiceDTOPut.class))).willReturn(criado);

        // ---- when + then ----
        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)          // "meu corpo é JSON"
                        .content(objectMapper.writeValueAsString(paraEnviar))) // objeto Java -> JSON. É o oposto do que o @RequestBody faz no controller. Um serializa, o outro desserializa.
                .andExpect(status().isCreated())                          // 201
                .andExpect(jsonPath("$.orderID").exists())                // o id gerado veio?
                .andExpect(jsonPath("$.status").value("Enviado"));

        // ---- prova extra: o controller REALMENTE chamou o service? ----
        verify(orderService, times(1)).createOrder(any(OrderServiceDTOPut.class));
    }

    @Test
    @DisplayName("PUT /order/{id} atualiza e devolve 200")
    void updateOrder_UpdatesAndReturns200_WhenSuccessful() throws Exception {
        // ---- given ----
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

        // ---- when + then ----
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
        // ---- given ----
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");

        // método void num mock já "não faz nada" por padrão;
        // deixamos explícito só pra ficar legível:
        willDoNothing().given(orderService).deleteOrder(id);

        // ---- when + then ----
        mockMvc.perform(delete("/order/{id}", id))
                .andExpect(status().isNoContent());   // 204 — e não há corpo pra verificar

        // a PROVA de que funcionou: o controller chamou deleteOrder(id) 1 vez
        verify(orderService, times(1)).deleteOrder(id);
    }

    @Test
    @DisplayName("GET-PAGE /order devolve 200 e uma página de pedidos")
    void getAllOrder_ReturnsPage_WhenSuccessful() throws Exception {
        // ---- given ----
        OrderServiceDTOPut dto = OrderServiceDTOPut.builder()
                .orderID(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .status("Enviado")
                .valueTotal(456.12)
                .date(LocalDateTime.now())
                .build();

        // uma "página" fake contendo 1 pedido
        Page<OrderServiceDTOPut> pagina = new PageImpl<>(List.of(dto));

        given(orderService.getAllOrder(any(Pageable.class))).willReturn(pagina);

        // ---- when + then ----
        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))               // a lista tem 1 item
                .andExpect(jsonPath("$.content[0].status").value("Enviado"))// 1º item, campo status
                .andExpect(jsonPath("$.content[0].valueTotal").value(456.12));
    }
}

/**
 * 2. any(OrderServiceDTOPut.class) — por que não passar o objeto exato?
 *
 * No GET usamos getOrderById(id) com o id exato, porque era fácil garantir igualdade. Aqui o DTO faz uma "viagem de ida e volta" (objeto → JSON → objeto novo dentro do controller), e o objeto que chega no service não é a mesma instância que criamos. Então dizemos ao Mockito:
 *
 * "Não importa qual OrderServiceDTOPut chegue — pra qualquer um, devolva o criado."
 *
 * Isso é um argument matcher. any(X.class) = "qualquer objeto do tipo X".
 *
 * */

















