package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.FornecedorRequestDTO;
import org.example.exception.BusinessRuleException;
import org.example.model.Fornecedor;
import org.example.service.FornecedorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.example.exception.ResourceNotFoundException;

@WebMvcTest(FornecedorController.class)
class FornecedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FornecedorService fornecedorService;

    @Test
    void deveCriarFornecedorComSucesso() throws Exception {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Novo Fornecedor");

        Fornecedor fornecedorSalvo = new Fornecedor("Novo Fornecedor");
        fornecedorSalvo.setId(1L);

        given(fornecedorService.criarFornecedor(any(FornecedorRequestDTO.class))).willReturn(fornecedorSalvo);

        mockMvc.perform(post("/api/fornecedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/fornecedores/1"));
    }

    @Test
    void naoDeveCriarFornecedorDuplicado() throws Exception {
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Fornecedor Existente");

        given(fornecedorService.criarFornecedor(any(FornecedorRequestDTO.class)))
                .willThrow(new BusinessRuleException("Um fornecedor com o mesmo nome já existe."));

        mockMvc.perform(post("/api/fornecedores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarFornecedorComSucesso() throws Exception {
        Long fornecedorId = 1L;
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Nome Atualizado");

        Fornecedor fornecedorAtualizado = new Fornecedor("Nome Atualizado");
        fornecedorAtualizado.setId(fornecedorId);

        given(fornecedorService.atualizarFornecedor(eq(fornecedorId), any(FornecedorRequestDTO.class))).willReturn(fornecedorAtualizado);

        mockMvc.perform(put("/api/fornecedores/{id}", fornecedorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));
    }

    @Test
    void deveDeletarFornecedorComSucesso() throws Exception {
        Long fornecedorId = 1L;
        willDoNothing().given(fornecedorService).deletarFornecedor(fornecedorId);

        mockMvc.perform(delete("/api/fornecedores/{id}", fornecedorId))
                .andExpect(status().isNoContent());
    }

    @Test
    void naoDeveDeletarFornecedorEmUso() throws Exception {
        Long fornecedorId = 1L;

        willThrow(new BusinessRuleException("Não é possível excluir um fornecedor que está sendo usado por produtos."))
                .given(fornecedorService).deletarFornecedor(fornecedorId);

        mockMvc.perform(delete("/api/fornecedores/{id}", fornecedorId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarNotFoundAoTentarDeletarFornecedorInexistente() throws Exception {
        Long idInexistente = 999L;

        willThrow(new ResourceNotFoundException("Fornecedor não encontrado com o ID: " + idInexistente))
                .given(fornecedorService).deletarFornecedor(idInexistente);

        mockMvc.perform(delete("/api/fornecedores/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }
}
