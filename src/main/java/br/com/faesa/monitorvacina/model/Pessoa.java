package br.com.faesa.monitorvacina.model;

import java.time.LocalDate;

// classe abstrata Pessoa, o molde pra outras classes
public abstract class Pessoa {
    
    protected String nome;
    protected String cpf;
    protected LocalDate dataNascimento;

    // getters e setters pra pegar e mudar os dados
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}