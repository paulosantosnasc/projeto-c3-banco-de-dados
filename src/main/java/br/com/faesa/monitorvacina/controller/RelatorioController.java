package br.com.faesa.monitorvacina.controller;

// 1. IMPORTAÇÕES ANTIGAS REMOVIDAS (java.sql.*)
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

// 2. NOVAS IMPORTAÇÕES DO MONGODB
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.AggregateIterable; // Para rodar JOINs e GROUP BYs
import org.bson.Document; // O "JSON" do MongoDB
import org.bson.conversions.Bson; // Para os comandos
import java.util.Arrays; // Para criar listas de comandos

// NOSSOS IMPORTS
import br.com.faesa.monitorvacina.conexion.ConexaoMongoDB; // Trocamos a conexão!


// classe que vai cuidar dos relatórios que o professor pediu no edital
public class RelatorioController {

    // Pega a conexão com o banco MongoDB
    private MongoDatabase db = ConexaoMongoDB.getConexao();
    
    // Para os relatórios, podemos precisar de todas as coleções
    // (Vamos pegar a coleção específica dentro de cada método)

    /**
     * Relatório 1 (com JOIN), pra mostrar o nome do usuário e da vacina em cada registro.
     * SQL Antigo:
     * SELECT u.nome, v.nome, r.descricaoUsuario
     * FROM RegistroEfeitoAdverso r
     * JOIN Usuario u ON r.idUsuario = u.idUsuario
     * JOIN Vacina v ON r.idVacina = v.idVacina
     * * MongoDB Novo (Aggregation com $lookup):
     */
    public void gerarRelatorioRegistrosPorUsuario() {
        
        System.out.println("\n--- RELATÓRIO: Efeitos Adversos por Usuário (MongoDB) ---");
        
        // Pega a coleção principal que queremos consultar
        MongoCollection<Document> colRegistro = db.getCollection("RegistroEfeitoAdverso");

        try {
            // A "Agregação" é o comando do MongoDB para fazer coisas complexas (JOIN, GROUP BY, etc)
            // Vamos construir o "pipeline" (passo a passo)
            AggregateIterable<Document> resultado = colRegistro.aggregate(Arrays.asList(
                
                // Passo 1: Fazer o JOIN com a coleção 'usuario'
                // (Equivalente ao: JOIN Usuario u ON r.idUsuario = u.idUsuario)
                new Document("$lookup", 
                    new Document("from", "usuario")         // A tabela que queremos "joinar"
                    .append("localField", "idUsuario")  // A "FK" na tabela RegistroEfeitoAdverso
                    .append("foreignField", "_id")      // A "PK" na tabela usuario
                    .append("as", "infoUsuario")),      // Nome do "array" temporário
                
                // Passo 2: Fazer o JOIN com a coleção 'vacina'
                // (Equivalente ao: JOIN Vacina v ON r.idVacina = v.idVacina)
                new Document("$lookup", 
                    new Document("from", "vacina")
                    .append("localField", "idVacina")
                    .append("foreignField", "_id")
                    .append("as", "infoVacina")),

                // Passo 3: O $lookup cria um ARRAY (ex: infoUsuario: [...] ). 
                // O $unwind "desmonta" esse array, transformando-o em um objeto só.
                new Document("$unwind", "$infoUsuario"),
                new Document("$unwind", "$infoVacina"),

                // Passo 4: Fazer o "SELECT" (Projetar) apenas os campos que queremos
                // (Equivalente ao: SELECT u.nome, v.nome, r.descricaoUsuario)
                new Document("$project", 
                    new Document("_id", 0) // Não mostrar o ID do registro
                    .append("nome_usuario", "$infoUsuario.nome") // Pega o nome de dentro do objeto joinado
                    .append("nome_vacina", "$infoVacina.nome")   // Pega o nome de dentro do objeto joinado
                    .append("descricao", "$descricaoUsuario")) // Pega a descrição da tabela original
            ));

            boolean encontrouResultado = false;
            // Loop para mostrar os resultados
            for (Document doc : resultado) {
                encontrouResultado = true;
                String nomeUsuario = doc.getString("nome_usuario");
                String nomeVacina = doc.getString("nome_vacina");
                String descricao = doc.getString("descricao");
                
                System.out.println("Usuário: " + nomeUsuario + 
                                   " | Vacina: " + nomeVacina + 
                                   " | Descrição: " + (descricao != null ? descricao : "N/A"));
            }
            
            if(!encontrouResultado) {
                System.out.println("Nenhum registro de efeito adverso encontrado.");
            }

        } catch (Exception e) {
            System.err.println("!! ERRO ao gerar relatório de registros (MongoDB): " + e.getMessage());
        }
        System.out.println("-------------------------------------------------");
    }

    /**
     * Relatório 2 (com GROUP BY), pra contar quantos registros cada vacina tem.
     * SQL Antigo:
     * SELECT v.nome, COUNT(r.idRegistro)
     * FROM Vacina v
     * LEFT JOIN RegistroEfeitoAdverso r ON v.idVacina = r.idVacina
     * GROUP BY v.nome
     * * MongoDB Novo (Aggregation com $lookup e $project/$size):
     */
    public void gerarRelatorioContagemPorVacina() {

        System.out.println("\n--- RELATÓRIO: Contagem de Efeitos por Vacina (MongoDB) ---");
        
        // Pega a coleção principal que queremos consultar (Vacina)
        MongoCollection<Document> colVacina = db.getCollection("vacina");

        try {
            AggregateIterable<Document> resultado = colVacina.aggregate(Arrays.asList(
                
                // Passo 1: Fazer o LEFT JOIN com a coleção 'RegistroEfeitoAdverso'
                // (Equivalente ao: LEFT JOIN RegistroEfeitoAdverso r ON v.idVacina = r.idVacina)
                new Document("$lookup",
                    new Document("from", "RegistroEfeitoAdverso")
                    .append("localField", "_id")
                    .append("foreignField", "idVacina")
                    .append("as", "registros_da_vacina")), // Array de todos os efeitos daquela vacina

                
                
                new Document("$project",
                    new Document("_id", 0) // Não mostrar o ID da vacina
                    .append("nome_vacina", "$nome") // Pega o nome da vacina
                    // O $size conta quantos elementos tem o array "registros_da_vacina"
                    // (É o nosso COUNT(*))
                    .append("total_registros", new Document("$size", "$registros_da_vacina")))
            ));
            
            boolean encontrouResultado = false;
            // Loop para mostrar os resultados
            for (Document doc : resultado) {
                encontrouResultado = true;
                String nomeVacina = doc.getString("nome_vacina");
                int total = doc.getInteger("total_registros");
                
                System.out.println("Vacina: " + nomeVacina + " | Total de Registros: " + total);
            }
            
            if(!encontrouResultado) {
                System.out.println("Nenhuma vacina encontrada para gerar o relatório.");
            }

        } catch (Exception e) {
            System.err.println("!! ERRO ao gerar relatório de contagem (MongoDB): " + e.getMessage());
        }
        System.out.println("-------------------------------------------------");
    }
}