package org.example.repository;

import org.example.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Adicione este método se ele não existir
    Optional<Categoria> findByNomeNormalizado(String nomeNormalizado);
}