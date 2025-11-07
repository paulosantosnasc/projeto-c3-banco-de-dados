package br.com.faesa.monitorvacina.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {


    

    private static final String DATABASE_NAME = "monitor_vacina_db"; 
    
    // 2. O endereço do servidor. Como tá na nossa máquina, é "localhost"
    private static final String HOST = "jdbc:mysql://localhost:3306/" + DATABASE_NAME; 
    
    private static final String USER = "root"; 
    
    private static final String PASSWORD = "Tartaruga157@"; 

    public static Connection getConexao() {
        Connection conexao = null; // Começa como nula
        
        try {
            conexao = DriverManager.getConnection(HOST, USER, PASSWORD);
            
            System.out.println("--> Conectou no banco de dados!");
            
        } catch (SQLException e) {
            System.err.println("!! ERRO ao conectar no banco: " + e.getMessage());
        }
        
        return conexao;
    }
}