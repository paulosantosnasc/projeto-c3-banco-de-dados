package br.com.faesa.monitorvacina.principal;

import br.com.faesa.monitorvacina.conexion.ConexaoMySQL;
import br.com.faesa.monitorvacina.conexion.ConexaoMongoDB;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document; // Importante: classe que representa um "documento" MongoDB

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ATENÇÃO:
 * Este é o script do ITEM 4 do Edital C3.
 * Ele deve ser executado APENAS UMA VEZ para copiar os dados do C2 (MySQL)
 * para o C3 (MongoDB).
 * * ANTES DE RODAR:
 * 1. Verifique se o seu servidor MySQL está rodando.
 * 2. Verifique se o seu servidor MongoDB está rodando.
 */
public class Migracao {

    public static void main(String[] args) {
        System.out.println("### INICIANDO SCRIPT DE MIGRAÇÃO (C2 -> C3) ###");
        
        try {
            // 1. Pega as conexões
            // (O try-with-resources garante que o conMySQL fecha no final)
            try (Connection conMySQL = ConexaoMySQL.getConexao()) {
                MongoDatabase dbMongo = ConexaoMongoDB.getConexao();
                
                if (conMySQL == null) {
                    System.err.println("!! FALHA NA MIGRAÇÃO: Não foi possível conectar ao MySQL.");
                    return;
                }
                if (dbMongo == null) {
                    System.err.println("!! FALHA NA MIGRAÇÃO: Não foi possível conectar ao MongoDB.");
                    return;
                }

                // 2. Roda as migrações (uma para cada tabela)
                migrarUsuarios(conMySQL, dbMongo);
                migrarVacinas(conMySQL, dbMongo);
                migrarRegistrosEfeitosAdversos(conMySQL, dbMongo);
            
                System.out.println("\n### MIGRAÇÃO CONCLUÍDA COM SUCESSO! ###");
                System.out.println("Verifique seu MongoDB Compass para ver os dados.");
            
            } catch (SQLException e) {
                System.err.println("!! ERRO GERAL DE SQL DURANTE A MIGRAÇÃO: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.err.println("!! ERRO GERAL DURANTE A MIGRAÇÃO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 3. Fecha a conexão do MongoDB ao final da migração
            ConexaoMongoDB.fecharConexao();
        }
    }

    /**
     * Lê a tabela `Usuario` do MySQL e insere na coleção `usuario` do MongoDB.
     */
    private static void migrarUsuarios(Connection conMySQL, MongoDatabase dbMongo) throws SQLException {
        System.out.println("\n... Migrando tabela USUARIO ...");
        
        // 1. Limpa a coleção antiga no MongoDB (para não duplicar dados se rodar de novo)
        dbMongo.getCollection("usuario").drop();
        
        // 2. Pega a coleção (tabela) no MongoDB
        MongoCollection<Document> colUsuario = dbMongo.getCollection("usuario");
        
        // 3. Prepara a leitura (SELECT) no MySQL
        String sql = "SELECT * FROM Usuario";
        
        try (PreparedStatement pstmt = conMySQL.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            int contador = 0;
            // 4. Loop: Lê cada linha do MySQL
            while (rs.next()) {
                
                // 5. Cria um "Documento" (JSON) para o MongoDB
                Document doc = new Document();
                
                // 6. Mapeia as colunas do MySQL para campos no Documento
                // IMPORTANTE: Vamos usar o ID do MySQL como _id no MongoDB
                // Isso vai facilitar muito nossos relacionamentos (FKs)
                doc.append("_id", rs.getInt("idUsuario")); 
                doc.append("nome", rs.getString("nome"));
                doc.append("cpf", rs.getString("cpf"));
                doc.append("dataNascimento", rs.getDate("dataNascimento")); // Salva como data
                doc.append("email", rs.getString("email"));
                doc.append("senha", rs.getString("senha"));
                
                // 7. Insere o documento novo no MongoDB
                colUsuario.insertOne(doc);
                contador++;
            }
            System.out.println("-> " + contador + " usuários migrados com sucesso!");
        }
    }

    /**
     * Lê a tabela `Vacina` do MySQL e insere na coleção `vacina` do MongoDB.
     */
    private static void migrarVacinas(Connection conMySQL, MongoDatabase dbMongo) throws SQLException {
        System.out.println("\n... Migrando tabela VACINA ...");
        
        dbMongo.getCollection("vacina").drop();
        MongoCollection<Document> colVacina = dbMongo.getCollection("vacina");
        
        String sql = "SELECT * FROM Vacina";
        
        try (PreparedStatement pstmt = conMySQL.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            int contador = 0;
            while (rs.next()) {
                Document doc = new Document();
                doc.append("_id", rs.getInt("idVacina")); // Mapeia PK para _id
                doc.append("nome", rs.getString("nome"));
                doc.append("fabricante", rs.getString("fabricante"));
                
                colVacina.insertOne(doc);
                contador++;
            }
            System.out.println("-> " + contador + " vacinas migradas com sucesso!");
        }
    }

    /**
     * Lê a tabela `RegistroEfeitoAdverso` do MySQL e insere na coleção `registroEfeitoAdverso` do MongoDB.
     */
    private static void migrarRegistrosEfeitosAdversos(Connection conMySQL, MongoDatabase dbMongo) throws SQLException {
        System.out.println("\n... Migrando tabela REGISTROEFEITOADVERSO ...");
        
        // ATENÇÃO: O nome da sua coleção no Mongo deve ser o mesmo nome da tabela SQL
        dbMongo.getCollection("RegistroEfeitoAdverso").drop();
        MongoCollection<Document> colRegistro = dbMongo.getCollection("RegistroEfeitoAdverso");
        
        String sql = "SELECT * FROM RegistroEfeitoAdverso";
        
        try (PreparedStatement pstmt = conMySQL.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            int contador = 0;
            while (rs.next()) {
                Document doc = new Document();
                doc.append("_id", rs.getInt("idRegistro")); // Mapeia PK para _id
                doc.append("dataVacinacao", rs.getDate("dataVacinacao"));
                doc.append("dataRegistro", rs.getTimestamp("dataRegistro")); // Timestamp é mais preciso
                doc.append("descricaoUsuario", rs.getString("descricaoUsuario"));

                // Mantemos as "chaves estrangeiras" como simples números.
                // Elas se referem aos _id que definimos em 'usuario' e 'vacina'.
                doc.append("idUsuario", rs.getInt("idUsuario"));
                doc.append("idVacina", rs.getInt("idVacina"));
                
                colRegistro.insertOne(doc);
                contador++;
            }
            System.out.println("-> " + contador + " registros de efeitos adversos migrados com sucesso!");
        }
    }
}