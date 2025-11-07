package br.com.faesa.monitorvacina.controller;


import java.util.ArrayList;
import java.util.List;

// 2. NOVAS IMPORTAÇÕES DO MONGODB
import org.bson.Document; // O "JSON" do MongoDB
import org.bson.conversions.Bson; // Para filtros e updates
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.FindIterable;
import static com.mongodb.client.model.Filters.eq; // Filtro de "igual a" (ex: WHERE id = ?)
import static com.mongodb.client.model.Updates.set; // Para o "SET" do update

// NOSSOS IMPORTS
import br.com.faesa.monitorvacina.conexion.ConexaoMongoDB; // Trocamos a conexão!
import br.com.faesa.monitorvacina.model.Usuario;
import java.time.ZoneId; // Para converter LocalDate para Date (do MongoDB)
import java.util.Date; // java.util.Date (usado pelo MongoDB)


// essa é a classe que vai ter a lógica pra mexer com o Usuario NO MONGODB
public class UsuarioController implements IController {

    // Pega a conexão com o banco MongoDB
    private MongoDatabase db = ConexaoMongoDB.getConexao();
    // Pega a "tabela" (coleção) específica de usuários
    private MongoCollection<Document> colUsuario = db.getCollection("usuario");

    
    private int getProximoId() {
        // 1. Acha o último documento, ordenando pelo _id de forma decrescente
        Document ultimoUsuario = colUsuario.find().sort(new Document("_id", -1)).first();
        
        if (ultimoUsuario == null) {
            // Coleção está vazia, começa do 1
            return 1; 
        } else {
            // Pega o _id do último usuário e soma 1
            return ultimoUsuario.getInteger("_id") + 1;
        }
    }


    // aqui a gente implementa o método "inserir" que veio do "contrato" IController
    @Override
    public void inserir(Object obj) {
        
        // a gente recebe um "Object", mas sabemos que é um Usuario, então convertemos
        Usuario novoUsuario = (Usuario) obj;

        try {
            // 1. Pega o próximo ID vago (ex: 5 + 1 = 6)
            int novoId = getProximoId();
            novoUsuario.setIdUsuario(novoId); // Guarda o ID no objeto
            
            // 2. Cria um "Documento" (JSON) para o MongoDB
            Document doc = new Document();
            
            // 3. Mapeia os dados do objeto Usuario para o Documento
            doc.append("_id", novoId); // FORÇAMOS o _id a ser nosso novo ID inteiro
            doc.append("nome", novoUsuario.getNome());
            doc.append("cpf", novoUsuario.getCpf());
            // Converte LocalDate (Java moderno) para Date (que o Mongo entende)
            doc.append("dataNascimento", Date.from(novoUsuario.getDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            doc.append("email", novoUsuario.getEmail());
            doc.append("senha", novoUsuario.getSenha());
            
            // 4. Manda inserir o documento no MongoDB
            System.out.println("Mandando o INSERT para o MongoDB...");
            colUsuario.insertOne(doc);
            System.out.println("Usuário '" + novoUsuario.getNome() + "' (ID: " + novoId + ") salvo!");

        } catch (Exception e) {
            System.err.println("!! ERRO ao salvar usuário no MongoDB: " + e.getMessage());
        }
        // 5. Não precisamos mais de "finally" para fechar conexão,
        // o driver do MongoDB gerencia isso sozinho.
    }

    // método pra atualizar um usuário que já existe no banco
    @Override
    public void atualizar(Object obj) {
        Usuario usuarioParaAtualizar = (Usuario) obj;
        
        try {
            // 1. Define o FILTRO: "Qual usuário eu quero atualizar?"
            // é o equivalente ao "WHERE idUsuario = ?"
            Bson filtro = eq("_id", usuarioParaAtualizar.getIdUsuario());
            
            // 2. Define os DADOS: "O que eu quero mudar?"
            // é o equivalente ao "SET nome = ?, cpf = ?, ..."
            List<Bson> updates = new ArrayList<>();
            updates.add(set("nome", usuarioParaAtualizar.getNome()));
            updates.add(set("cpf", usuarioParaAtualizar.getCpf()));
            updates.add(set("dataNascimento", Date.from(usuarioParaAtualizar.getDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant())));
            updates.add(set("email", usuarioParaAtualizar.getEmail()));
            updates.add(set("senha", usuarioParaAtualizar.getSenha()));
            
            // 3. Combina todos os "SET" em um comando só
            Bson dadosNovos = com.mongodb.client.model.Updates.combine(updates);

            // 4. Manda executar o UPDATE no MongoDB
            System.out.println("Mandando o UPDATE para o MongoDB...");
            UpdateResult resultado = colUsuario.updateOne(filtro, dadosNovos);
            
            // 5. Verifica se o update realmente achou e mudou alguém
            if (resultado.getModifiedCount() > 0) {
                System.out.println("Usuário com ID " + usuarioParaAtualizar.getIdUsuario() + " foi atualizado!");
            } else {
                System.out.println("!! AVISO: Nenhum usuário encontrado com o ID " + usuarioParaAtualizar.getIdUsuario() + ". Nada foi atualizado.");
            }

        } catch (Exception e) {
            System.err.println("!! ERRO ao atualizar usuário no MongoDB: " + e.getMessage());
        }
    }

    // método pra apagar um usuário do banco
    @Override
    public void remover(Object obj) {
        Usuario usuarioParaRemover = (Usuario) obj;
        
        try {
            // 1. Define o FILTRO: "Qual usuário eu quero apagar?"
            // é o equivalente ao "WHERE idUsuario = ?"
            Bson filtro = eq("_id", usuarioParaRemover.getIdUsuario());
            
            // 2. Manda executar o DELETE no MongoDB
            System.out.println("Mandando o DELETE para o MongoDB...");
            DeleteResult resultado = colUsuario.deleteOne(filtro);

            // 3. Verifica se o delete realmente achou e apagou alguém
            if (resultado.getDeletedCount() > 0) {
                System.out.println("Usuário com ID " + usuarioParaRemover.getIdUsuario() + " foi removido!");
            } else {
                System.out.println("!! AVISO: Nenhum usuário encontrado com o ID " + usuarioParaRemover.getIdUsuario() + ". Nada foi removido.");
            }

        } catch (Exception e) {
            System.err.println("!! ERRO ao remover usuário no MongoDB: " + e.getMessage());
        }
    }

    // método pra buscar todos os usuários no banco
    @Override
    public List<Object> listarTodos() {
        List<Object> listaUsuarios = new ArrayList<>();
        
        try {
            // 1. Manda buscar TODOS os documentos da coleção
            // é o equivalente ao "SELECT * FROM Usuario"
            FindIterable<Document> documentos = colUsuario.find();
            
            System.out.println("Buscando lista de usuários no MongoDB...");

            // 2. Loop: Para cada Documento que o MongoDB achou...
            for (Document doc : documentos) {
                
                // 3. ...converte o Documento de volta para um objeto Usuario
                Usuario usuario = new Usuario();
                
                usuario.setIdUsuario(doc.getInteger("_id"));
                usuario.setNome(doc.getString("nome"));
                usuario.setCpf(doc.getString("cpf"));
                // Converte Date (do Mongo) de volta para LocalDate (do Java moderno)
                usuario.setDataNascimento(doc.getDate("dataNascimento").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                usuario.setEmail(doc.getString("email"));
                usuario.setSenha(doc.getString("senha"));
                
                // 4. Adiciona o objeto Usuario na lista de retorno
                listaUsuarios.add(usuario);
            }

        } catch (Exception e) {
            System.err.println("!! ERRO ao listar usuários do MongoDB: " + e.getMessage());
        }
        
        return listaUsuarios;
    }
}