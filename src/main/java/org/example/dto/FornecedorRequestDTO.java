package org.example.dto;

public class FornecedorRequestDTO {

    private String nome;

    public FornecedorRequestDTO() {
    }

    public FornecedorRequestDTO(String nome) {
        this.nome = nome;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}