package org.example.repository;

import org.example.model.Pedido;
import org.example.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de integração para a PedidoRepository.
 * A anotação @DataJpaTest configura um ambiente de teste focado na persistência JPA.
 */
@DataJpaTest
class PedidoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    void deveSalvarPedidoComProdutos() {
        // Arrange
        Produto produto1 = new Produto("Notebook", 4500.00);
        Produto produto2 = new Produto("Mouse sem Fio", 150.00);
        entityManager.persist(produto1);
        entityManager.persist(produto2);

        Pedido pedido = new Pedido(LocalDate.now());
        pedido.setProdutos(List.of(produto1, produto2));

        // Act
        pedidoRepository.save(pedido);
        entityManager.flush();
        entityManager.clear(); // Limpa o cache de persistência para garantir que estamos lendo do banco

        // Assert
        Pedido pedidoSalvo = entityManager.find(Pedido.class, pedido.getId());

        assertNotNull(pedidoSalvo);
        assertNotNull(pedidoSalvo.getId());
        assertEquals(2, pedidoSalvo.getProdutos().size());
        assertTrue(pedidoSalvo.getProdutos().stream().anyMatch(p -> p.getNome().equals("Notebook")));
        assertTrue(pedidoSalvo.getProdutos().stream().anyMatch(p -> p.getNome().equals("Mouse sem Fio")));
    }
}

