package br.com.faesa.monitorvacina.controller;

import java.util.ArrayList;
import java.util.List;

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
import br.com.faesa.monitorvacina.model.Vacina; // Importa o Model que acabámos de criar

// Este controller é uma cópia do UsuarioController, mas para Vacina
public class VacinaController implements IController {

    private MongoDatabase db = ConexaoMongoDB.getConexao();
    private MongoCollection<Document> colVacina = db.getCollection("vacina");

    private int getProximoId() {
        Document ultimaVacina = colVacina.find().sort(new Document("_id", -1)).first();
        if (ultimaVacina == null) {
            return 1; 
        } else {
            return ultimaVacina.getInteger("_id") + 1;
        }
    }

    @Override
    public void inserir(Object obj) {
        Vacina novaVacina = (Vacina) obj;
        try {
            int novoId = getProximoId();
            novaVacina.setIdVacina(novoId);
            
            Document doc = new Document();
            doc.append("_id", novoId);
            doc.append("nome", novaVacina.getNome());
            doc.append("fabricante", novaVacina.getFabricante());
            
            System.out.println("Mandando o INSERT (Vacina) para o MongoDB...");
            colVacina.insertOne(doc);
            System.out.println("Vacina '" + novaVacina.getNome() + "' (ID: " + novoId + ") salva!");

        } catch (Exception e) {
            System.err.println("!! ERRO ao salvar vacina no MongoDB: " + e.getMessage());
        }
    }

    @Override
    public void atualizar(Object obj) {
        Vacina vacinaParaAtualizar = (Vacina) obj;
        try {
            Bson filtro = eq("_id", vacinaParaAtualizar.getIdVacina());
            
            List<Bson> updates = new ArrayList<>();
            updates.add(set("nome", vacinaParaAtualizar.getNome()));
            updates.add(set("fabricante", vacinaParaAtualizar.getFabricante()));
            
            Bson dadosNovos = com.mongodb.client.model.Updates.combine(updates);

            System.out.println("Mandando o UPDATE (Vacina) para o MongoDB...");
            UpdateResult resultado = colVacina.updateOne(filtro, dadosNovos);
            
            if (resultado.getModifiedCount() > 0) {
                System.out.println("Vacina com ID " + vacinaParaAtualizar.getIdVacina() + " foi atualizada!");
            } else {
                System.out.println("!! AVISO: Nenhuma vacina encontrada com o ID " + vacinaParaAtualizar.getIdVacina() + ". Nada foi atualizado.");
            }
        } catch (Exception e) {
            System.err.println("!! ERRO ao atualizar vacina no MongoDB: " + e.getMessage());
        }
    }

    @Override
    public void remover(Object obj) {
        Vacina vacinaParaRemover = (Vacina) obj;
        try {
            Bson filtro = eq("_id", vacinaParaRemover.getIdVacina());
            
            System.out.println("Mandando o DELETE (Vacina) para o MongoDB...");
            DeleteResult resultado = colVacina.deleteOne(filtro);

            if (resultado.getDeletedCount() > 0) {
                System.out.println("Vacina com ID " + vacinaParaRemover.getIdVacina() + " foi removida!");
            } else {
                System.out.println("!! AVISO: Nenhuma vacina encontrada com o ID " + vacinaParaRemover.getIdVacina() + ". Nada foi removido.");
            }
        } catch (Exception e) {
            System.err.println("!! ERRO ao remover vacina no MongoDB: " + e.getMessage());
        }
    }

    @Override
    public List<Object> listarTodos() {
        List<Object> listaVacinas = new ArrayList<>();
        try {
            FindIterable<Document> documentos = colVacina.find();
            System.out.println("Buscando lista de vacinas no MongoDB...");

            for (Document doc : documentos) {
                Vacina vacina = new Vacina();
                
                vacina.setIdVacina(doc.getInteger("_id"));
                vacina.setNome(doc.getString("nome"));
                vacina.setFabricante(doc.getString("fabricante"));
                
                listaVacinas.add(vacina);
            }
        } catch (Exception e) {
            System.err.println("!! ERRO ao listar vacinas do MongoDB: " + e.getMessage());
        }
        return listaVacinas;
    }
}