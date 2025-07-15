package org.example.service;

import org.example.dto.PedidoRequestDTO;
import org.example.dto.PedidoUpdateRequestDTO;
import org.example.exception.BusinessRuleException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Pedido;
import org.example.model.Produto;
import org.example.repository.PedidoRepository;
import org.example.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Camada de serviço para a lógica de negócio relacionada a Pedidos.
 */
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * Cria um novo pedido e o associa aos produtos fornecidos.
     * @param pedidoDTO O DTO contendo a lista de IDs dos produtos.
     * @return O novo Pedido criado e salvo no banco de dados.
     * @throws ResourceNotFoundException se algum dos IDs de produto não for encontrado.
     * @throws BusinessRuleException se a lista de produtos estiver vazia.
     */
    @Transactional
    public Pedido criarPedido(PedidoRequestDTO pedidoDTO) {
        if (pedidoDTO.getProdutoIds() == null || pedidoDTO.getProdutoIds().isEmpty()) {
            throw new BusinessRuleException("Um pedido deve conter pelo menos um produto.");
        }

        // Busca todos os produtos da lista de uma só vez para otimização.
        List<Produto> produtos = produtoRepository.findAllById(pedidoDTO.getProdutoIds());

        // Verifica se todos os produtos solicitados foram encontrados.
        if (produtos.size() != pedidoDTO.getProdutoIds().size()) {
            // Lógica para encontrar qual ID não foi encontrado.
            List<Long> idsEncontrados = produtos.stream().map(Produto::getId).collect(Collectors.toList());
            List<Long> idsFaltantes = pedidoDTO.getProdutoIds().stream()
                    .filter(id -> !idsEncontrados.contains(id))
                    .collect(Collectors.toList());
            throw new ResourceNotFoundException("Produto(s) com ID(s) " + idsFaltantes + " não encontrado(s).");
        }

        Pedido novoPedido = new Pedido(LocalDate.now());
        produtos.forEach(novoPedido::adicionarProduto);

        return pedidoRepository.save(novoPedido);
    }

    /**
     * Atualiza um pedido existente, modificando sua lista de produtos e/ou data de entrega.
     * @param id O ID do pedido a ser atualizado.
     * @param pedidoDTO O DTO com os dados de atualização.
     * @return O Pedido atualizado.
     */
    @Transactional
    public Pedido atualizarPedido(Long id, PedidoUpdateRequestDTO pedidoDTO) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido com ID " + id + " não encontrado"));

        // Atualiza a lista de produtos se ela for fornecida no DTO
        if (pedidoDTO.getProdutoIds() != null && !pedidoDTO.getProdutoIds().isEmpty()) {
            List<Produto> produtos = produtoRepository.findAllById(pedidoDTO.getProdutoIds());
            if (produtos.size() != pedidoDTO.getProdutoIds().size()) {
                throw new ResourceNotFoundException("Um ou mais produtos não foram encontrados.");
            }
            // Limpa a lista antiga e adiciona os novos produtos
            pedidoExistente.getProdutos().clear();
            produtos.forEach(pedidoExistente::adicionarProduto);
        }

        // Atualiza a data de entrega se for fornecida
        if (pedidoDTO.getDataEntrega() != null) {
            pedidoExistente.setDataEntrega(pedidoDTO.getDataEntrega());
        }

        return pedidoRepository.save(pedidoExistente);
    }

    /**
     * Deleta um pedido do sistema.
     * @param id O ID do pedido a ser deletado.
     */
    @Transactional
    public void deletarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido com ID " + id + " não encontrado"));

        // Limpa a associação com produtos antes de deletar
        pedido.getProdutos().forEach(produto -> produto.getPedidos().remove(pedido));
        pedido.getProdutos().clear();

        pedidoRepository.delete(pedido);
    }

    /**
     * Busca um pedido pelo seu ID.
     * @param id O ID do pedido a ser buscado.
     * @return O Pedido correspondente ao ID fornecido.
     * @throws ResourceNotFoundException se o pedido não for encontrado.
     */
    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido com ID " + id + " não encontrado"));
    }

    /**
     * Lista todos os pedidos do sistema.
     * @return Uma lista de Pedidos.
     */
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }
}
