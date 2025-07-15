package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.CategoriaRequestDTO;
import org.example.exception.BusinessRuleException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Categoria;
import org.example.service.CategoriaService;
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

/**
 * Teste de integração para o CategoriaController.
 * A anotação @WebMvcTest(CategoriaController.class) foca o teste apenas na camada web (o controller),
 * sem carregar o contexto completo da aplicação.
 * As dependências do controller, como os services, são substituídas por Mocks.
 */
@WebMvcTest(CategoriaController.class)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService categoriaService;

    @Test
    void deveCriarCategoriaComSucesso() throws Exception {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNome("Eletrônicos");

        Categoria categoriaSalva = new Categoria("Eletrônicos");
        categoriaSalva.setId(1L);

        // Configura o mock do service para retornar a categoria salva quando o método criar for chamado.
        given(categoriaService.criarCategoria(any(CategoriaRequestDTO.class))).willReturn(categoriaSalva);

        // Executa a requisição POST e verifica a resposta.
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/categorias/1"))
                .andExpect(jsonPath("$.nome").value("Eletrônicos"));
    }

    @Test
    void naoDeveCriarCategoriaDuplicada() throws Exception {
        CategoriaRequestDTO dto = new CategoriaRequestDTO();
        dto.setNome("Eletrônicos");

        // Configura o mock do service para lançar a exceção de regra de negócio.
        given(categoriaService.criarCategoria(any(CategoriaRequestDTO.class)))
                .willThrow(new BusinessRuleException("Uma categoria com nome similar já existe"));

        // Executa a requisição POST e verifica se a resposta é um 400 Bad Request.
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveDeletarCategoriaComSucesso() throws Exception {
        Long categoriaId = 1L;

        // Configura o mock do service para não fazer nada quando o método deletar for chamado.
        // Isso simula a exclusão bem-sucedida.
        willDoNothing().given(categoriaService).deletarCategoria(categoriaId);

        // Executa a requisição DELETE e verifica se a resposta é 204 No Content.
        mockMvc.perform(delete("/api/categorias/{id}", categoriaId))
                .andExpect(status().isNoContent());
    }

    @Test
    void naoDeveDeletarCategoriaEmUso() throws Exception {
        Long categoriaId = 1L;

        // Configura o mock do service para lançar a exceção de regra de negócio.
        willThrow(new BusinessRuleException("Não é possível excluir uma categoria que está sendo usada por produtos."))
                .given(categoriaService).deletarCategoria(categoriaId);

        // Executa a requisição DELETE e verifica se a resposta é 400 Bad Request.
        mockMvc.perform(delete("/api/categorias/{id}", categoriaId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarNotFoundAoTentarDeletarCategoriaInexistente() throws Exception {
        Long idInexistente = 999L;

        // Configura o mock do service para lançar a exceção de recurso não encontrado.
        willThrow(new ResourceNotFoundException("Categoria não encontrada com o ID: " + idInexistente))
                .given(categoriaService).deletarCategoria(idInexistente);

        // Executa a requisição DELETE e verifica se a resposta é 404 Not Found.
        mockMvc.perform(delete("/api/categorias/{id}", idInexistente))
                .andExpect(status().isNotFound());
    }
}
