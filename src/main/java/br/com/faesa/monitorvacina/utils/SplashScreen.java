package br.com.faesa.monitorvacina.utils;


import com.mongodb.client.MongoDatabase;
import br.com.faesa.monitorvacina.conexion.ConexaoMongoDB; // Trocamos a conexão!

public class SplashScreen {

    // método que a gente vai chamar pra mostrar a tela de início
    public void exibir() {

        // pega a contagem de cada tabela (agora usando MongoDB)
        int totalUsuarios = contarRegistros("usuario");
        int totalVacinas = contarRegistros("vacina");
        int totalRegistrosEfeitos = contarRegistros("RegistroEfeitoAdverso");
        
        System.out.println("############################################################");
        System.out.println("#");
        System.out.println("#       SISTEMA DE MONITORAMENTO DE EFEITOS ADVERSOS");
        System.out.println("#                  (Versão MongoDB - C3)");
        System.out.println("#");
        System.out.println("############################################################");
        System.out.println("#");
        System.out.println("# TOTAL DE DOCUMENTOS EXISTENTES:");
        System.out.println("# 1 - USUARIOS: " + totalUsuarios);
        System.out.println("# 2 - VACINAS: " + totalVacinas);
        System.out.println("# 3 - REGISTROS DE EFEITOS: " + totalRegistrosEfeitos);
        System.out.println("#");
        System.out.println("############################################################");
        System.out.println("# CRIADO POR:");
        System.out.println("# - Paulo Henrique do Nascimento");
        System.out.println("# - Luis Felipe Andrade");
        System.out.println("# - Murillo Daré");
        System.out.println("# - Eduardo Gobbi");
        System.out.println("# - pedro henrique pire");
        System.out.println("#");
        System.out.println("############################################################");
        System.out.println("# DISCIPLINA: BANCO DE DADOS");
        System.out.println("# PROFESSOR: Howard Roatti");
        System.out.println("############################################################\n");
    }
    
    /**
     * Método privado que faz a contagem de registros (documentos) em uma coleção específica.
     * AGORA USANDO MONGODB.
     */
    private int contarRegistros(String nomeColecao) {
        
        long total = 0; // countDocuments retorna 'long'
        
        try {
            // 1. Pega a conexão com o MongoDB
            MongoDatabase db = ConexaoMongoDB.getConexao();
            
            // 2. Pega a coleção (tabela)
            // 3. Conta quantos documentos (linhas) existem nela
            total = db.getCollection(nomeColecao).countDocuments();

        } catch (Exception e) {
            System.err.println("!! ERRO ao contar registros da coleção " + nomeColecao + ": " + e.getMessage());
            // Se der erro (ex: coleção não existe), retorna 0
        }
        
        return (int) total; // Converte o 'long' para 'int'
    }
}