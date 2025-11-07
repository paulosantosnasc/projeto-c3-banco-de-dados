package br.com.faesa.monitorvacina.model;

import java.time.LocalDate;

// Usuario herda de Pessoa, ou seja, Usuario "é uma" Pessoa
public class Usuario extends Pessoa {

    // o que só Usuario tem
    private int idUsuario;
    private String email;
    private String senha;

    // construtor vazio
    public Usuario() {
    }

    // getters e setters dos atributos de Usuario
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    // pra gente poder imprimir o objeto e ver o que tem dentro, ajuda a testar
    @Override
    public String toString() {
        // pega o nome e cpf que vieram da classe Pessoa
        return "Usuario [id=" + idUsuario + ", nome=" + nome + ", cpf=" + cpf + "]";
    }
}