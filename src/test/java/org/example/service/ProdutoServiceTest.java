package org.example.service;

import org.example.dto.ProdutoRequestDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Categoria;
import org.example.model.Fornecedor;
import org.example.model.Produto;
import org.example.repository.CategoriaRepository;
import org.example.repository.FornecedorRepository;
import org.example.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock private ProdutoRepository produtoRepository;
    @Mock private CategoriaRepository categoriaRepository;
    @Mock private FornecedorRepository fornecedorRepository;
    @InjectMocks private ProdutoService produtoService;

    private Categoria categoria;
    private Fornecedor fornecedor;
    private ProdutoRequestDTO produtoRequestDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria("Eletrônicos");
        categoria.setId(1L);
        fornecedor = new Fornecedor("Fornecedor Tech");
        fornecedor.setId(1L);

        produtoRequestDTO = new ProdutoRequestDTO();
        produtoRequestDTO.setNome("Notebook");
        produtoRequestDTO.setPreco(4000.00);
        produtoRequestDTO.setCategoriaId(1L);
        produtoRequestDTO.setFornecedorId(1L);
    }

    // ... (outros testes que já estão passando)

    @Test
    void naoDeveCriarProdutoComCategoriaInexistente() {
        produtoRequestDTO.setCategoriaId(999L);
        given(categoriaRepository.findById(999L)).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            produtoService.criarProduto(produtoRequestDTO);
        });
        // --- CORREÇÃO AQUI ---
        assertEquals("Categoria com ID 999 não encontrada", exception.getMessage()); // Mensagem exata, sem ponto final.
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    void naoDeveCriarProdutoComFornecedorInexistente() {
        produtoRequestDTO.setFornecedorId(999L);
        given(categoriaRepository.findById(1L)).willReturn(Optional.of(categoria));
        given(fornecedorRepository.findById(999L)).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            produtoService.criarProduto(produtoRequestDTO);
        });
        // --- CORREÇÃO AQUI ---
        assertEquals("Fornecedor com ID 999 não encontrado", exception.getMessage()); // Mensagem exata, sem ponto final.
        verify(produtoRepository, never()).save(any(Produto.class));
    }
}