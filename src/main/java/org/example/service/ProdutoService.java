package org.example.service;

import org.example.dto.ProdutoRequestDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Categoria;
import org.example.model.Fornecedor;
import org.example.model.Produto;
import org.example.repository.CategoriaRepository;
import org.example.repository.FornecedorRepository;
import org.example.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Transactional
    public Produto criarProduto(ProdutoRequestDTO produtoDTO) {
        Categoria categoria = categoriaRepository.findById(produtoDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria com ID " + produtoDTO.getCategoriaId() + " não encontrada"));
        Fornecedor fornecedor = fornecedorRepository.findById(produtoDTO.getFornecedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor com ID " + produtoDTO.getFornecedorId() + " não encontrado"));

        Produto novoProduto = new Produto(produtoDTO.getNome(), produtoDTO.getPreco());
        novoProduto.setCategoria(categoria);
        novoProduto.setFornecedor(fornecedor);

        return produtoRepository.save(novoProduto);
    }

    @Transactional
    public Produto atualizarProduto(Long id, ProdutoRequestDTO produtoDTO) {
        Produto produtoExistente = buscarPorId(id);

        produtoExistente.setNome(produtoDTO.getNome());
        produtoExistente.setPreco(produtoDTO.getPreco());

        if (produtoDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(produtoDTO.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria com ID " + produtoDTO.getCategoriaId() + " não encontrada"));
            produtoExistente.setCategoria(categoria);
        }

        if (produtoDTO.getFornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(produtoDTO.getFornecedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fornecedor com ID " + produtoDTO.getFornecedorId() + " não encontrado"));
            produtoExistente.setFornecedor(fornecedor);
        }

        return produtoRepository.save(produtoExistente);
    }

    @Transactional
    public void deletarProduto(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado"));
    }
}