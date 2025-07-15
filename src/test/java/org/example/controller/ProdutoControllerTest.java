package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ProdutoRequestDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Categoria;
import org.example.model.Fornecedor;
import org.example.model.Produto;
import org.example.service.ProdutoService;
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

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutoService produtoService;

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Teclado Mecânico");
        dto.setPreco(350.00);
        dto.setCategoriaId(1L);
        dto.setFornecedorId(1L);

        Produto produtoSalvo = new Produto("Teclado Mecânico", 350.00);
        produtoSalvo.setId(1L);
        produtoSalvo.setCategoria(new Categoria("Eletrônicos"));
        produtoSalvo.setFornecedor(new Fornecedor("Fornecedor Tech"));

        given(produtoService.criarProduto(any(ProdutoRequestDTO.class))).willReturn(produtoSalvo);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/produtos/1"))
                .andExpect(jsonPath("$.nome").value("Teclado Mecânico"));
    }

    @Test
    void naoDeveCriarProdutoSeCategoriaNaoExiste() throws Exception {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Teclado Mecânico");
        dto.setPreco(350.00);
        dto.setCategoriaId(999L);
        dto.setFornecedorId(1L);

        given(produtoService.criarProduto(any(ProdutoRequestDTO.class)))
                .willThrow(new ResourceNotFoundException("Categoria com ID 999 não encontrada"));

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarProdutoComSucesso() throws Exception {
        Long produtoId = 1L;
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Novo Nome");
        dto.setPreco(199.99);

        Produto produtoAtualizado = new Produto("Novo Nome", 199.99);
        produtoAtualizado.setId(produtoId);

        given(produtoService.atualizarProduto(eq(produtoId), any(ProdutoRequestDTO.class))).willReturn(produtoAtualizado);

        mockMvc.perform(put("/api/produtos/{id}", produtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Nome"));
    }

    @Test
    void deveDeletarProdutoComSucesso() throws Exception {
        Long produtoId = 1L;

        willDoNothing().given(produtoService).deletarProduto(produtoId);

        mockMvc.perform(delete("/api/produtos/{id}", produtoId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarProdutoInexistente() throws Exception {
        Long produtoId = 999L;

        willThrow(new ResourceNotFoundException("Produto não encontrado com o ID: " + produtoId))
                .given(produtoService).deletarProduto(produtoId);

        mockMvc.perform(delete("/api/produtos/{id}", produtoId))
                .andExpect(status().isNotFound());
    }
}