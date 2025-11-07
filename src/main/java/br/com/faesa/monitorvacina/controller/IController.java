package br.com.faesa.monitorvacina.controller;

import java.util.List;


public interface IController {
    
    // método pra salvar um objeto novo no banco
    void inserir(Object obj);
    
    // pra alterar um objeto que já existe
    void atualizar(Object obj);
    
    // pra apagar um objeto do banco
    void remover(Object obj);
    
    // pra listar tudo de uma tabela
    List<Object> listarTodos();
    
}