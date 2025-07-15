package org.example.repository;

import org.example.model.Categoria;
import org.example.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProdutoRepository produtoRepository;

    private Categoria eletronicos;
    private Categoria livros;

    @BeforeEach
    void setUp() {
        eletronicos = new Categoria("Eletrônicos");
        livros = new Categoria("Livros");
        entityManager.persist(eletronicos);
        entityManager.persist(livros);

        Produto p1 = new Produto("Notebook Gamer", 5000.00);
        p1.setCategoria(eletronicos);
        entityManager.persist(p1);

        Produto p2 = new Produto("Smartphone", 3000.00);
        p2.setCategoria(eletronicos);
        entityManager.persist(p2);

        Produto p3 = new Produto("O Codificador Limpo", 150.00);
        p3.setCategoria(livros);
        entityManager.persist(p3);

        Produto p4 = new Produto("Domain-Driven Design", 200.00);
        p4.setCategoria(livros);
        entityManager.persist(p4);

        entityManager.flush();
    }

    @Test
    void deveEncontrarProdutosComPrecoMaiorQue() {
        List<Produto> produtos = produtoRepository.findComPrecoMaiorQue(4000.00);
        assertEquals(1, produtos.size());
        assertEquals("Notebook Gamer", produtos.get(0).getNome());
    }

    @Test
    void deveContarProdutosPorCategoria() {
        long totalEletronicos = produtoRepository.countByCategoria(eletronicos);
        assertEquals(2, totalEletronicos);
    }

    @Test
    void deveEncontrarProdutosPorParteDoNomeOuCategoria() {
        List<Produto> encontradosPeloNome = produtoRepository.findPorNomeOuCategoriaContendo("note");
        assertEquals(1, encontradosPeloNome.size());

        List<Produto> encontradosPelaCategoria = produtoRepository.findPorNomeOuCategoriaContendo("livro");
        assertEquals(2, encontradosPelaCategoria.size());
    }

    @Test
    void deveEncontrarTop5ProdutosMaisCarosComQueryNativa() {
        Produto p5 = new Produto("Macbook Pro", 15000.00);
        p5.setCategoria(eletronicos);
        entityManager.persistAndFlush(p5);

        List<Produto> topProdutos = produtoRepository.findTop5MaisCarosNativa();
        assertEquals(5, topProdutos.size());
        assertEquals("Macbook Pro", topProdutos.get(0).getNome());
        assertEquals("Notebook Gamer", topProdutos.get(1).getNome());
    }
}

