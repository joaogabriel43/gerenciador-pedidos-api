package org.example.dto;

import java.util.List;

/**
 * DTO (Data Transfer Object) para receber os dados de uma requisição de criação de Pedido.
 * Contém uma lista de IDs dos produtos que devem ser associados ao novo pedido.
 */
public class PedidoRequestDTO {

    private List<Long> produtoIds;

    public List<Long> getProdutoIds() {
        return produtoIds;
    }

    public void setProdutoIds(List<Long> produtoIds) {
        this.produtoIds = produtoIds;
    }
}

