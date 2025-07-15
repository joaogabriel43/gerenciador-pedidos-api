package org.example.model;

import jakarta.persistence.*; // Importe tudo de jakarta.persistence
import java.text.Normalizer; // Importe o Normalizer

@Entity
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String nomeNormalizado; // <- CAMPO ADICIONADO

    // Construtores (o seu já está bom)
    public Fornecedor(String nome) {
        this.nome = nome;
    }

    public Fornecedor() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeNormalizado() { // <- GETTER ADICIONADO
        return nomeNormalizado;
    }

    // Lógica de normalização automática
    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        if (this.nome != null) {
            this.nomeNormalizado = Normalizer.normalize(this.nome, Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                    .toLowerCase();
        }
    }
}