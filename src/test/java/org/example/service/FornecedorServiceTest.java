package org.example.service;

import org.example.dto.FornecedorRequestDTO;
import org.example.exception.BusinessRuleException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Fornecedor;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Teste de unidade para a FornecedorService.
 */
@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private FornecedorService fornecedorService;

    private Fornecedor fornecedor;

    @BeforeEach
    void setUp() {
        fornecedor = new Fornecedor("Fornecedor Tech");
        fornecedor.setId(1L);
    }

    @Test
    void deveCriarFornecedorComSucesso() {
        // Arrange
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Fornecedor Tech");
        given(fornecedorRepository.findByNomeNormalizado(anyString())).willReturn(Optional.empty());
        given(fornecedorRepository.save(any(Fornecedor.class))).willReturn(fornecedor);

        // Act
        Fornecedor fornecedorCriado = fornecedorService.criarFornecedor(dto);

        // Assert
        assertNotNull(fornecedorCriado);
        assertEquals("Fornecedor Tech", fornecedorCriado.getNome());
        verify(fornecedorRepository, times(1)).save(any(Fornecedor.class));
    }

    @Test
    void naoDeveCriarFornecedorComNomeDuplicado() {
        given(fornecedorRepository.findByNomeNormalizado(anyString())).willReturn(Optional.of(fornecedor));
        BusinessRuleException e = assertThrows(BusinessRuleException.class, () -> fornecedorService.criarFornecedor(new FornecedorRequestDTO("Fornecedor Tech")));
        assertEquals("Um fornecedor com nome similar já existe: " + fornecedor.getNome(), e.getMessage());
        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    void deveAtualizarFornecedorComSucesso() {
        // Arrange
        Long fornecedorId = 1L;
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Novo Nome Fornecedor");
        given(fornecedorRepository.findById(fornecedorId)).willReturn(Optional.of(fornecedor));
        given(fornecedorRepository.findByNomeNormalizado("novo nome fornecedor")).willReturn(Optional.empty());
        given(fornecedorRepository.save(any(Fornecedor.class))).willAnswer(invocation -> invocation.getArgument(0));

        // Act
        Fornecedor fornecedorAtualizado = fornecedorService.atualizarFornecedor(fornecedorId, dto);

        // Assert
        assertEquals("Novo Nome Fornecedor", fornecedorAtualizado.getNome());
        verify(fornecedorRepository, times(1)).save(fornecedor);
    }

    @Test
    void naoDeveAtualizarFornecedorInexistente() {
        // Arrange
        Long idInexistente = 999L;
        FornecedorRequestDTO dto = new FornecedorRequestDTO();
        dto.setNome("Qualquer Nome");
        given(fornecedorRepository.findById(idInexistente)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            fornecedorService.atualizarFornecedor(idInexistente, dto);
        });
        verify(fornecedorRepository, never()).save(any(Fornecedor.class));
    }

    @Test
    void deveDeletarFornecedorComSucesso() {
        // Arrange
        Long fornecedorId = 1L;
        given(fornecedorRepository.findById(fornecedorId)).willReturn(Optional.of(fornecedor));
        given(produtoRepository.countByFornecedor(fornecedor)).willReturn(0L);
        doNothing().when(fornecedorRepository).delete(fornecedor);

        // Act
        assertDoesNotThrow(() -> fornecedorService.deletarFornecedor(fornecedorId));

        // Assert
        verify(fornecedorRepository, times(1)).delete(fornecedor);
    }

    @Test
    void naoDeveDeletarFornecedorEmUso() {
        given(fornecedorRepository.findById(1L)).willReturn(Optional.of(fornecedor));
        given(produtoRepository.countByFornecedor(fornecedor)).willReturn(3L);
        BusinessRuleException e = assertThrows(BusinessRuleException.class, () -> fornecedorService.deletarFornecedor(1L));
        assertEquals("Não é possível excluir um fornecedor que está sendo usado por produtos.", e.getMessage());
        verify(fornecedorRepository, never()).delete(any());
    }

    @Test
    void naoDeveDeletarFornecedorInexistente() {
        // Arrange
        Long idInexistente = 999L;
        given(fornecedorRepository.findById(idInexistente)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            fornecedorService.deletarFornecedor(idInexistente);
        });
        verify(fornecedorRepository, never()).delete(any(Fornecedor.class));
    }
}
