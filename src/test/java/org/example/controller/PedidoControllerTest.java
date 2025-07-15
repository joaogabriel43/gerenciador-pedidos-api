package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.PedidoRequestDTO;
import org.example.dto.PedidoUpdateRequestDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Pedido;
import org.example.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoService pedidoService;

    @Test
    void deveCriarPedidoComSucesso() throws Exception {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setProdutoIds(List.of(1L, 2L));

        Pedido pedidoSalvo = new Pedido(LocalDate.now());
        pedidoSalvo.setId(1L);

        given(pedidoService.criarPedido(any(PedidoRequestDTO.class))).willReturn(pedidoSalvo);

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/pedidos/1"));
    }

    @Test
    void naoDeveCriarPedidoQuandoProdutoNaoEncontrado() throws Exception {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setProdutoIds(List.of(1L, 999L));

        given(pedidoService.criarPedido(any(PedidoRequestDTO.class)))
                .willThrow(new ResourceNotFoundException("Produto(s) com ID(s) [999] não encontrado(s)."));

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarPedidoComSucesso() throws Exception {
        Long pedidoId = 1L;
        PedidoUpdateRequestDTO dto = new PedidoUpdateRequestDTO();
        dto.setProdutoIds(List.of(3L));
        dto.setDataEntrega(LocalDate.of(2025, 12, 25));

        Pedido pedidoAtualizado = new Pedido(LocalDate.now());
        pedidoAtualizado.setId(pedidoId);
        pedidoAtualizado.setDataEntrega(dto.getDataEntrega());

        given(pedidoService.atualizarPedido(eq(pedidoId), any(PedidoUpdateRequestDTO.class))).willReturn(pedidoAtualizado);

        mockMvc.perform(put("/api/pedidos/{id}", pedidoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataEntrega").value("2025-12-25"));
    }

    @Test
    void deveDeletarPedidoComSucesso() throws Exception {
        Long pedidoId = 1L;
        // Configura o mock para não fazer nada (void) quando o método deletar for chamado.
        doNothing().when(pedidoService).deletarPedido(pedidoId);

        mockMvc.perform(delete("/api/pedidos/{id}", pedidoId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoTentarDeletarPedidoInexistente() throws Exception {
        Long pedidoId = 999L;
        // Configura o mock para lançar a exceção quando o método deletar for chamado com o ID inexistente.
        doThrow(new ResourceNotFoundException("Pedido com ID " + pedidoId + " não encontrado"))
                .when(pedidoService).deletarPedido(pedidoId);

        mockMvc.perform(delete("/api/pedidos/{id}", pedidoId))
                .andExpect(status().isNotFound());
    }
}
