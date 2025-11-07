package br.com.faesa.monitorvacina.model;

public class Vacina {

    private int idVacina; // No MongoDB, vamos usar isso como o _id
    private String nome;
    private String fabricante;

    // Construtor vazio
    public Vacina() {
    }

    // Getters e Setters
    public int getIdVacina() {
        return idVacina;
    }

    public void setIdVacina(int idVacina) {
        this.idVacina = idVacina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    // Método toString para ajudar a listar (Opção 4 do menu)
    @Override
    public String toString() {
        return "Vacina [id=" + idVacina + ", nome=" + nome + ", fabricante=" + (fabricante != null ? fabricante : "N/A") + "]";
    }
}