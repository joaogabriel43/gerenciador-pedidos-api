package org.example.repository;

import org.example.model.Categoria;
import org.example.util.NormalizadorUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoriaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void deveSalvarCategoriaComNomeNormalizado() {
        Categoria categoria = new Categoria("Eletrônicos");
        entityManager.persistAndFlush(categoria);

        Categoria categoriaSalva = entityManager.find(Categoria.class, categoria.getId());
        assertEquals("eletronico", categoriaSalva.getNomeNormalizado());
    }

    @Test
    void deveEncontrarCategoriaPeloNomeNormalizado() {
        Categoria categoria = new Categoria("Livros");
        entityManager.persistAndFlush(categoria);

        String nomeBusca = "LIVRO"; // Busca por nome diferente, mas que normaliza igual
        String nomeNormalizadoBusca = NormalizadorUtil.normalizar(nomeBusca);

        Optional<Categoria> categoriaEncontrada = categoriaRepository.findByNomeNormalizado(nomeNormalizadoBusca);

        assertTrue(categoriaEncontrada.isPresent());
        assertEquals("Livros", categoriaEncontrada.get().getNome());
    }

    @Test
    void naoDeveEncontrarCategoriaInexistente() {
        String nomeNormalizado = "inexistente";
        Optional<Categoria> categoriaEncontrada = categoriaRepository.findByNomeNormalizado(nomeNormalizado);
        assertFalse(categoriaEncontrada.isPresent());
    }
}

