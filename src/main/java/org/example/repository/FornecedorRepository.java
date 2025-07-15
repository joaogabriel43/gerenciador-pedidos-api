package org.example.repository;

import org.example.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    // Método correto que o teste espera
    Optional<Fornecedor> findByNomeNormalizado(String nomeNormalizado);
}