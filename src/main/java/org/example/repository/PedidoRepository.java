package org.example.repository;

import org.example.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // 6. Retorne pedidos que ainda não possuem uma data de entrega.
    List<Pedido> findByDataEntregaIsNull();

    // 7. Retorne pedidos com data de entrega preenchida.
    List<Pedido> findByDataEntregaIsNotNull();

    // 13. Retorne pedidos feitos após uma data específica.
    List<Pedido> findByDataAfter(LocalDate data);

    // 14. Retorne pedidos feitos antes de uma data específica.
    List<Pedido> findByDataBefore(LocalDate data);

    // 15. Retorne pedidos feitos em um intervalo de datas.
    List<Pedido> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

    // JPQL
    // 5. Crie uma consulta que retorne os pedidos feitos entre duas datas.
    @Query("SELECT p FROM Pedido p WHERE p.data BETWEEN :dataInicio AND :dataFim")
    List<Pedido> findPedidosEntreDatas(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
}
