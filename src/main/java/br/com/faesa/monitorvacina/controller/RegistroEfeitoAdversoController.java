package br.com.faesa.monitorvacina.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Date; // java.util.Date (usado pelo MongoDB)
import java.time.ZoneId; // Para conversões de data

import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import br.com.faesa.monitorvacina.conexion.ConexaoMongoDB;
import br.com.faesa.monitorvacina.model.RegistroEfeitoAdverso; // Importa o Model que acabámos de criar

public class RegistroEfeitoAdversoController implements IController {

    private MongoDatabase db = ConexaoMongoDB.getConexao();
    private MongoCollection<Document> colRegistro = db.getCollection("RegistroEfeitoAdverso");

    private int getProximoId() {
        Document ultimoRegistro = colRegistro.find().sort(new Document("_id", -1)).first();
        if (ultimoRegistro == null) {
            return 1; 
        } else {
            return ultimoRegistro.getInteger("_id") + 1;
        }
    }

    @Override
    public void inserir(Object obj) {
        RegistroEfeitoAdverso novoRegistro = (RegistroEfeitoAdverso) obj;
        try {
            int novoId = getProximoId();
            novoRegistro.setIdRegistro(novoId);
            
            Document doc = new Document();
            doc.append("_id", novoId);
            
            // Converte LocalDate (Java) para Date (Mongo)
            doc.append("dataVacinacao", Date.from(novoRegistro.getDataVacinacao().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            // Converte LocalDateTime (Java) para Date (Mongo)
            doc.append("dataRegistro", Date.from(novoRegistro.getDataRegistro().atZone(ZoneId.systemDefault()).toInstant()));
            doc.append("descricaoUsuario", novoRegistro.getDescricaoUsuario());
            
            // Armazena as "FKs"
            doc.append("idUsuario", novoRegistro.getIdUsuario());
            doc.append("idVacina", novoRegistro.getIdVacina());
            
            System.out.println("Mandando o INSERT (Registro) para o MongoDB...");
            colRegistro.insertOne(doc);
            System.out.println("Registro (ID: " + novoId + ") salvo!");

        } catch (Exception e) {
            System.err.println("!! ERRO ao salvar registro no MongoDB: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Object obj) {
        RegistroEfeitoAdverso registroParaAtualizar = (RegistroEfeitoAdverso) obj;
        try {
            Bson filtro = eq("_id", registroParaAtualizar.getIdRegistro());
            
            List<Bson> updates = new ArrayList<>();
            updates.add(set("dataVacinacao", Date.from(registroParaAtualizar.getDataVacinacao().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            updates.add(set("dataRegistro", Date.from(registroParaAtualizar.getDataRegistro().atZone(ZoneId.systemDefault()).toInstant())));
            updates.add(set("descricaoUsuario", registroParaAtualizar.getDescricaoUsuario()));
            updates.add(set("idUsuario", registroParaAtualizar.getIdUsuario()));
            updates.add(set("idVacina", registroParaAtualizar.getIdVacina()));

            Bson dadosNovos = com.mongodb.client.model.Updates.combine(updates);

            System.out.println("Mandando o UPDATE (Registro) para o MongoDB...");
            UpdateResult resultado = colRegistro.updateOne(filtro, dadosNovos);
            
            if (resultado.getModifiedCount() > 0) {
                System.out.println("Registro com ID " + registroParaAtualizar.getIdRegistro() + " foi atualizado!");
            } else {
                System.out.println("!! AVISO: Nenhum registro encontrado com o ID " + registroParaAtualizar.getIdRegistro() + ". Nada foi atualizado.");
            }
        } catch (Exception e) {
            System.err.println("!! ERRO ao atualizar registro no MongoDB: " + e.getMessage());
        }
    }

    @Override
    public void remover(Object obj) {
        RegistroEfeitoAdverso registroParaRemover = (RegistroEfeitoAdverso) obj;
        
      
        
        try {
            Bson filtro = eq("_id", registroParaRemover.getIdRegistro());
            
            System.out.println("Mandando o DELETE (Registro) para o MongoDB...");
            DeleteResult resultado = colRegistro.deleteOne(filtro);

            if (resultado.getDeletedCount() > 0) {
                System.out.println("Registro com ID " + registroParaRemover.getIdRegistro() + " foi removido!");
            } else {
                System.out.println("!! AVISO: Nenhum registro encontrado com o ID " + registroParaRemover.getIdRegistro() + ". Nada foi removido.");
            }
        } catch (Exception e) {
            System.err.println("!! ERRO ao remover registro no MongoDB: " + e.getMessage());
        }
    }

    @Override
    public List<Object> listarTodos() {
        List<Object> listaRegistros = new ArrayList<>();
        try {
            FindIterable<Document> documentos = colRegistro.find();
            System.out.println("Buscando lista de registros no MongoDB...");

            for (Document doc : documentos) {
                RegistroEfeitoAdverso registro = new RegistroEfeitoAdverso();
                
                registro.setIdRegistro(doc.getInteger("_id"));
                // Converte Date (Mongo) para LocalDate (Java)
                registro.setDataVacinacao(doc.getDate("dataVacinacao").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                // Converte Date (Mongo) para LocalDateTime (Java)
                registro.setDataRegistro(doc.getDate("dataRegistro").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                registro.setDescricaoUsuario(doc.getString("descricaoUsuario"));
                registro.setIdUsuario(doc.getInteger("idUsuario"));
                registro.setIdVacina(doc.getInteger("idVacina"));
                
                listaRegistros.add(registro);
            }
        } catch (Exception e) {
            System.err.println("!! ERRO ao listar registros do MongoDB: " + e.getMessage());
        }
        return listaRegistros;
    }
}