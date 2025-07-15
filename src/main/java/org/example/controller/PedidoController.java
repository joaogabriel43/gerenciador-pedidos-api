package org.example.controller;

import org.example.dto.PedidoRequestDTO;
import org.example.dto.PedidoUpdateRequestDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Pedido;
import org.example.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller para gerenciar as operações RESTful da entidade Pedido.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Lista todos os pedidos existentes, incluindo os produtos associados a cada um.
     * Utiliza uma consulta otimizada para evitar o problema de N+1 selects.
     * @return Uma lista de todos os pedidos.
     */
    @GetMapping
    public List<Pedido> listarTodos() {
        // Esta consulta será otimizada para buscar os produtos junto com os pedidos.
        return pedidoService.listarPedidos();
    }

    /**
     * Busca um pedido específico pelo seu ID, incluindo os produtos associados.
     * @param id O ID do pedido a ser buscado.
     * @return Uma ResponseEntity com o pedido encontrado (200 OK) ou um status 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPedidoPorId(id));
    }

    /**
     * Cria um novo pedido a partir de uma lista de IDs de produtos.
     * @param pedidoDTO O DTO contendo a lista de IDs de produtos.
     * @return Uma ResponseEntity com o novo pedido criado e o status 201 Created.
     */
    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody PedidoRequestDTO pedidoDTO) {
        Pedido novoPedido = pedidoService.criarPedido(pedidoDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoPedido.getId()).toUri();
        return ResponseEntity.created(location).body(novoPedido);
    }

    /**
     * Atualiza um pedido existente.
     * @param id O ID do pedido a ser atualizado.
     * @param pedidoDTO O DTO com os dados para atualização.
     * @return Uma ResponseEntity com o pedido atualizado (200 OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody PedidoUpdateRequestDTO pedidoDTO) {
        Pedido pedidoAtualizado = pedidoService.atualizarPedido(id, pedidoDTO);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    /**
     * Deleta um pedido existente.
     * @param id O ID do pedido a ser deletado.
     * @return Uma ResponseEntity com o status 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
