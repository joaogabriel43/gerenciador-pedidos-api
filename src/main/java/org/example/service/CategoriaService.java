package org.example.service;

import org.example.dto.CategoriaRequestDTO;
import org.example.exception.BusinessRuleException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Categoria;
import org.example.repository.CategoriaRepository;
import org.example.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer; // IMPORT ADICIONADO
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // Método utilitário para normalizar nomes
    private String normalizarNome(String nome) {
        if (nome == null) return null;
        return Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria com ID " + id + " não encontrada"));
    }

    @Transactional
    public Categoria criarCategoria(CategoriaRequestDTO categoriaDTO) {
        // --- LÓGICA DE VALIDAÇÃO CORRIGIDA ---
        String nomeNormalizado = normalizarNome(categoriaDTO.getNome());
        Optional<Categoria> categoriaExistente = categoriaRepository.findByNomeNormalizado(nomeNormalizado);

        if (categoriaExistente.isPresent()) {
            throw new BusinessRuleException("Uma categoria com nome similar já existe: " + categoriaExistente.get().getNome());
        }

        Categoria novaCategoria = new Categoria(categoriaDTO.getNome());
        return categoriaRepository.save(novaCategoria);
    }

    @Transactional
    public Categoria atualizarCategoria(Long id, CategoriaRequestDTO categoriaDTO) {
        Categoria categoriaExistente = buscarPorId(id);

        // --- LÓGICA DE VALIDAÇÃO CORRIGIDA ---
        String nomeNormalizado = normalizarNome(categoriaDTO.getNome());
        Optional<Categoria> outraCategoriaComMesmoNome = categoriaRepository.findByNomeNormalizado(nomeNormalizado);

        if (outraCategoriaComMesmoNome.isPresent() && !outraCategoriaComMesmoNome.get().getId().equals(id)) {
            throw new BusinessRuleException("Uma categoria com nome similar já existe: " + outraCategoriaComMesmoNome.get().getNome());
        }

        categoriaExistente.setNome(categoriaDTO.getNome());
        return categoriaRepository.save(categoriaExistente);
    }

    @Transactional
    public void deletarCategoria(Long id) {
        Categoria categoria = buscarPorId(id);

        long produtosVinculados = produtoRepository.countByCategoria(categoria);
        if (produtosVinculados > 0) {
            // Ajustando a mensagem para ser genérica, o que facilita o teste
            throw new BusinessRuleException("Não é possível excluir uma categoria que está sendo usada por produtos.");
        }

        categoriaRepository.delete(categoria);
    }
}