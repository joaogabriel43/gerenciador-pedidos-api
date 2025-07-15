package org.example.service;

import org.example.dto.PedidoRequestDTO;
import org.example.dto.PedidoUpdateRequestDTO;
import org.example.exception.BusinessRuleException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.repository.PedidoRepository;
import org.example.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Teste de unidade para a PedidoService.
 */
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Produto produto1;
    private Produto produto2;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        produto1 = new Produto("Produto Teste 1", 100.00);
        produto1.setId(1L);
        produto2 = new Produto("Produto Teste 2", 200.00);
        produto2.setId(2L);

        pedido = new Pedido(LocalDate.now());
        pedido.setId(1L);

        // --- ALTERAÇÃO PRINCIPAL AQUI ---
        // Envolvemos List.of() em um new ArrayList<>() para criar uma lista MUTÁVEL.
        pedido.setProdutos(new ArrayList<>(List.of(produto1)));
    }

    @Test
    void deveCriarPedidoComSucesso() {
        // Arrange
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setProdutoIds(List.of(1L, 2L));
        given(produtoRepository.findAllById(dto.getProdutoIds())).willReturn(List.of(produto1, produto2));
        given(pedidoRepository.save(any(Pedido.class))).willAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            p.setId(1L); // Simula a geração de ID
            // Garante que o objeto salvo também tenha uma lista mutável
            p.setProdutos(new ArrayList<>(p.getProdutos()));
            return p;
        });

        // Act
        Pedido pedidoCriado = pedidoService.criarPedido(dto);

        // Assert
        assertNotNull(pedidoCriado);
        assertEquals(2, pedidoCriado.getProdutos().size());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void naoDeveCriarPedidoComProdutoInexistente() {
        // Arrange
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setProdutoIds(List.of(1L, 999L)); // ID 999 não existe
        // Simula que o repositório só encontrou o produto de ID 1
        given(produtoRepository.findAllById(dto.getProdutoIds())).willReturn(List.of(produto1));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.criarPedido(dto);
        });
        assertEquals("Produto(s) com ID(s) [999] não encontrado(s).", exception.getMessage());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void naoDeveCriarPedidoSemProdutos() {
        PedidoRequestDTO dto = new PedidoRequestDTO();
        dto.setProdutoIds(Collections.emptyList());
        BusinessRuleException e = assertThrows(BusinessRuleException.class, () -> pedidoService.criarPedido(dto));
        assertEquals("Um pedido deve conter pelo menos um produto.", e.getMessage()); // Corrigido para ter o ponto final.
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveAtualizarPedidoComSucesso() {
        // Arrange
        Long pedidoId = 1L;
        PedidoUpdateRequestDTO dto = new PedidoUpdateRequestDTO();
        dto.setProdutoIds(List.of(2L)); // Troca o produto
        dto.setDataEntrega(LocalDate.now().plusDays(5));

        given(pedidoRepository.findById(pedidoId)).willReturn(Optional.of(pedido));
        given(produtoRepository.findAllById(dto.getProdutoIds())).willReturn(List.of(produto2));
        given(pedidoRepository.save(any(Pedido.class))).willAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pedido pedidoAtualizado = pedidoService.atualizarPedido(pedidoId, dto);

        // Assert
        assertEquals(1, pedidoAtualizado.getProdutos().size());
        assertTrue(pedidoAtualizado.getProdutos().contains(produto2));
        assertEquals(LocalDate.now().plusDays(5), pedidoAtualizado.getDataEntrega());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void deveDeletarPedidoComSucesso() {
        // Arrange
        Long pedidoId = 1L;
        given(pedidoRepository.findById(pedidoId)).willReturn(Optional.of(pedido));
        doNothing().when(pedidoRepository).delete(pedido);

        // Act
        assertDoesNotThrow(() -> pedidoService.deletarPedido(pedidoId));

        // Assert
        verify(pedidoRepository, times(1)).delete(pedido);
    }

    @Test
    void naoDeveDeletarPedidoInexistente() {
        // Arrange
        Long idInexistente = 999L;
        given(pedidoRepository.findById(idInexistente)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.deletarPedido(idInexistente);
        });
        verify(pedidoRepository, never()).delete(any(Pedido.class));
    }
}
