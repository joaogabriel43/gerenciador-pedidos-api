package org.example.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para receber os dados de uma requisição de atualização de Pedido.
 * Permite a atualização da lista de produtos e da data de entrega.
 */
public class PedidoUpdateRequestDTO {

    private List<Long> produtoIds;
    private LocalDate dataEntrega;

    public List<Long> getProdutoIds() {
        return produtoIds;
    }

    public void setProdutoIds(List<Long> produtoIds) {
        this.produtoIds = produtoIds;
    }

    public LocalDate getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDate dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
}

