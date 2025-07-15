package org.example.repository;

import org.example.model.Categoria;
import org.example.model.Produto;
import org.example.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // 1. Retorne todos os produtos com o nome exato fornecido.
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    // 2. Retorne todos os produtos associados a uma categoria específica.
    List<Produto> findByCategoria(Categoria categoria);

    // 3. Retorne produtos com preço maior que o valor fornecido.
    List<Produto> findByPrecoGreaterThan(Double preco);

    // 4. Retorne produtos com preço menor que o valor fornecido.
    List<Produto> findByPrecoLessThan(Double preco);

    // 5. Retorne produtos cujo nome contenha o termo especificado.
    List<Produto> findByNomeContaining(String termo);

    // 8. Retorne produtos de uma categoria ordenados pelo preço de forma crescente.
    List<Produto> findByCategoriaOrderByPrecoAsc(Categoria categoria);

    // 9. Retorne produtos de uma categoria ordenados pelo preço de forma decrescente.
    List<Produto> findByCategoriaOrderByPrecoDesc(Categoria categoria);

    // 10. Retorne a contagem de produtos em uma categoria específica.
    long countByCategoria(Categoria categoria);

    long countByFornecedor(Fornecedor fornecedor);

    // 11. Retorne a contagem de produtos cujo preço seja maior que o valor fornecido.
    long countByPrecoGreaterThan(Double preco);

    // 12. Retorne produtos com preço menor que o valor fornecido ou cujo nome contenha o termo especificado.
    List<Produto> findByPrecoLessThanOrNomeContaining(Double preco, String nome);

    // 16. Retorne os três produtos mais caros.
    List<Produto> findTop3ByOrderByPrecoDesc();

    // 17. Retorne os cinco produtos mais baratos de uma categoria.
    List<Produto> findTop5ByCategoriaOrderByPrecoAsc(Categoria categoria);

    // JPQL Queries
    // 1. Crie uma consulta que retorne os produtos com preço maior que um valor
    @Query("SELECT p FROM Produto p WHERE p.preco > :preco")
    List<Produto> findComPrecoMaiorQue(@Param("preco") Double preco);

    // 2. Crie uma consulta que retorne os produtos ordenados pelo preço crescente.
    @Query("SELECT p FROM Produto p ORDER BY p.preco ASC")
    List<Produto> findTodosOrderByPrecoAsc();

    // 3. Crie uma consulta que retorne os produtos ordenados pelo preço decrescente.
    @Query("SELECT p FROM Produto p ORDER BY p.preco DESC")
    List<Produto> findTodosOrderByPrecoDesc();

    // 4. Crie uma consulta que retorne os produtos que comecem com uma letra específica.
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT(:prefixo, '%'))")
    List<Produto> findByNomeStartingWith(@Param("prefixo") String prefixo);

    // 6. Crie uma consulta que retorne a média de preços dos produtos.
    @Query("SELECT AVG(p.preco) FROM Produto p")
    Double findMediaPrecos();

    // 7. Crie uma consulta que retorne o preço máximo de um produto em uma categoria
    @Query("SELECT MAX(p.preco) FROM Produto p WHERE p.categoria = :categoria")
    Double findPrecoMaximoPorCategoria(@Param("categoria") Categoria categoria);

    // 8. Crie uma consulta para contar o número de produtos por categoria.
    @Query("SELECT p.categoria.nome, COUNT(p) FROM Produto p GROUP BY p.categoria.nome")
    List<Object[]> countProdutosPorCategoria();

    // Consulta otimizada para buscar produtos com suas associações (evita N+1)
    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.categoria LEFT JOIN FETCH p.fornecedor")
    List<Produto> findAllCompletos();

    // 10. Crie uma consulta para retornar os produtos filtrados por nome ou por categoria.
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(p.categoria.nome) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Produto> findPorNomeOuCategoriaContendo(@Param("termo") String termo);

    // 11. Crie uma consulta nativa para buscar os cinco produtos mais caros
    @Query(value = "SELECT * FROM produto ORDER BY valor DESC LIMIT 5", nativeQuery = true)
    List<Produto> findTop5MaisCarosNativa();
}
