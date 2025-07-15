package org.example.dto;

public class CategoriaRequestDTO {

    private String nome;

    // --- ADICIONE ESTE CONSTRUTOR ---
    public CategoriaRequestDTO() {
    }
    // ---------------------------------

    public CategoriaRequestDTO(String nome) {
        this.nome = nome;
    }

    // Getters e Setters...
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}