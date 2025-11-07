package br.com.faesa.monitorvacina.model;

import java.time.LocalDate; // Para dataVacinacao
import java.time.LocalDateTime; // Para dataRegistro

public class RegistroEfeitoAdverso {

    private int idRegistro; // _id no MongoDB
    private LocalDate dataVacinacao;
    private LocalDateTime dataRegistro;
    private String descricaoUsuario;
    
    // Para as "FKs"
    private int idUsuario;
    private int idVacina;
    
    // (Opcional) Para os relatórios, podemos guardar os objetos inteiros
    private Usuario usuario;
    private Vacina vacina;

    // Construtor vazio
    public RegistroEfeitoAdverso() {
    }

    // Getters e Setters (vou adicionar só os que precisamos para o CRUD)
    
    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public LocalDate getDataVacinacao() {
        return dataVacinacao;
    }

    public void setDataVacinacao(LocalDate dataVacinacao) {
        this.dataVacinacao = dataVacinacao;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getDescricaoUsuario() {
        return descricaoUsuario;
    }

    public void setDescricaoUsuario(String descricaoUsuario) {
        this.descricaoUsuario = descricaoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdVacina() {
        return idVacina;
    }

    public void setIdVacina(int idVacina) {
        this.idVacina = idVacina;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vacina getVacina() {
        return vacina;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    // Método toString para ajudar a listar
    @Override
    public String toString() {
        return "Registro [id=" + idRegistro + ", idUsuario=" + idUsuario + ", idVacina=" + idVacina + ", desc=" + (descricaoUsuario != null ? descricaoUsuario : "N/A") + "]";
    }
}