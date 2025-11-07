package br.com.faesa.monitorvacina.conexion;

// Importações necessárias para o driver do MongoDB
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;

public class ConexaoMongoDB {

    // Endereço padrão do MongoDB rodando na sua máquina
    private static final String HOST = "mongodb://localhost:27017";
    
    // Vamos manter o mesmo nome do banco para facilitar
    private static final String DATABASE_NAME = "monitor_vacina_db";
    
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    
    public static MongoDatabase getConexao() {
        
        // Se já tivermos uma conexão, apenas a retorna
        if (database != null) {
            return database;
        }
        
        try {
            // 1. Cria o Cliente (a ponte principal com o servidor MongoDB)
            mongoClient = MongoClients.create(HOST);
            
            // 2. Pega o banco de dados específico (ele é criado se não existir)
            database = mongoClient.getDatabase(DATABASE_NAME);
            
            System.out.println("--> Conectou no banco de dados MongoDB!");
            
        } catch (MongoException e) {
            System.err.println("!! ERRO ao conectar no MongoDB: " + e.getMessage());
        }
        
        return database;
    }

 
    public static void fecharConexao() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexão com MongoDB fechada.");
        }
    }
}