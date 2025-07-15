package org.example.repository;

import org.example.model.Fornecedor;
import org.example.util.NormalizadorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de integração para a FornecedorRepository.
 * A anotação @DataJpaTest configura um ambiente de teste focado na persistência JPA,
 * utilizando um banco de dados em memória e revertendo transações após cada teste.
 */
@DataJpaTest
class FornecedorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Test
    void deveSalvarFornecedorComNomeNormalizado() {
        // Arrange
        Fornecedor fornecedor = new Fornecedor("Fornecedor Tech");

        // Act
        entityManager.persistAndFlush(fornecedor);
        Fornecedor fornecedorSalvo = entityManager.find(Fornecedor.class, fornecedor.getId());

        // Assert
        assertNotNull(fornecedorSalvo);
        assertEquals("fornecedor tech", fornecedorSalvo.getNomeNormalizado());
    }

    @Test
    void deveEncontrarFornecedorPeloNomeNormalizado() {
        // Arrange
        Fornecedor fornecedor = new Fornecedor("Empresa de Logística");
        entityManager.persistAndFlush(fornecedor);

        String nomeBusca = "empresa de logistica"; // Busca pelo nome já normalizado
        String nomeNormalizadoBusca = NormalizadorUtil.normalizar(nomeBusca);

        // Act
        Optional<Fornecedor> fornecedorEncontrado = fornecedorRepository.findByNomeNormalizado(nomeNormalizadoBusca);

        // Assert
        assertTrue(fornecedorEncontrado.isPresent());
        assertEquals("Empresa de Logística", fornecedorEncontrado.get().getNome());
    }

    @Test
    void naoDeveEncontrarFornecedorInexistente() {
        // Arrange
        String nomeNormalizado = "inexistente";

        // Act
        Optional<Fornecedor> fornecedorEncontrado = fornecedorRepository.findByNomeNormalizado(nomeNormalizado);

        // Assert
        assertFalse(fornecedorEncontrado.isPresent());
    }
}

