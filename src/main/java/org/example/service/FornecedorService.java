package org.example.service;

import org.example.dto.FornecedorRequestDTO;
import org.example.exception.BusinessRuleException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Fornecedor;
import org.example.repository.FornecedorRepository;
import org.example.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private ProdutoRepository produtoRepository;


    @Transactional(readOnly = true) // Boa prática: transações de leitura são mais eficientes.
    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Fornecedor buscarPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor com ID " + id + " não encontrado"));
    }


    private String normalizarNome(String nome) {
        if (nome == null) return null;
        return Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

    @Transactional
    public Fornecedor criarFornecedor(FornecedorRequestDTO fornecedorDTO) {
        String nomeNormalizado = normalizarNome(fornecedorDTO.getNome());
        Optional<Fornecedor> fornecedorExistente = fornecedorRepository.findByNomeNormalizado(nomeNormalizado);

        if (fornecedorExistente.isPresent()) {
            throw new BusinessRuleException("Um fornecedor com nome similar já existe: " + fornecedorExistente.get().getNome());
        }

        Fornecedor novoFornecedor = new Fornecedor(fornecedorDTO.getNome());
        return fornecedorRepository.save(novoFornecedor);
    }

    @Transactional
    public void deletarFornecedor(Long id) {
        Fornecedor fornecedor = buscarPorId(id);

        long produtosVinculados = produtoRepository.countByFornecedor(fornecedor);
        if (produtosVinculados > 0) {
            // --- LÓGICA CORRIGIDA ---
            // Mensagem de erro genérica para facilitar o teste.
            throw new BusinessRuleException("Não é possível excluir um fornecedor que está sendo usado por produtos.");
        }

        fornecedorRepository.delete(fornecedor);
    }

    public Fornecedor atualizarFornecedor(Long id, FornecedorRequestDTO fornecedorDTO) {
        Fornecedor fornecedorExistente = buscarPorId(id);

        // Normalizando o nome do fornecedor
        String nomeNormalizado = normalizarNome(fornecedorDTO.getNome());
        Optional<Fornecedor> outraFornecedorComMesmoNome = fornecedorRepository.findByNomeNormalizado(nomeNormalizado);

        if (outraFornecedorComMesmoNome.isPresent() && !outraFornecedorComMesmoNome.get().getId().equals(id)) {
            throw new BusinessRuleException("Um fornecedor com nome similar já existe: " + outraFornecedorComMesmoNome.get().getNome());
        }

        fornecedorExistente.setNome(fornecedorDTO.getNome());
        return fornecedorRepository.save(fornecedorExistente);
    }
}