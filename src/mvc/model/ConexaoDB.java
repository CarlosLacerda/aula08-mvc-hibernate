package mvc.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * Conecta ao banco de dados
 * 
 */

// Centraliza a criação da conexão JDBC, evitando repetir a string de conexão em cada DAO.
public class ConexaoDB {

    // ============= CONFIGURAÇÃO DE ACESSO AO BANCO DE DADOS =================
	
    private static final String IPv4_SERVIDOR = "";  // PREENCHA AQUI COM O ENDEREÇO IPv4 FORNECIDO PELO PROFESSOR!
    
    private static final String PORTA = "3306";
    
    private static final String BANCO_DE_DADOS = "<RA_DO_ALUNO>";	// TROQUE POR SEU RA EM LETRAS MAIÚSCULAS E SEM TRAÇOS!

    private static final String USUARIO = "<RA_DO_ALUNO>";			// TROQUE POR SEU RA EM LETRAS MAIÚSCULAS E SEM TRAÇOS!
    
    private static final String SENHA = "<RA_DO_ALUNO>";			// TROQUE POR SEU RA EM LETRAS MAIÚSCULAS E SEM TRAÇOS!
    
    // ========================================================================

    // A URL usa o driver do MariaDB
    private static final String URL = "jdbc:mariadb://" + IPv4_SERVIDOR + ":" + PORTA + "/" + BANCO_DE_DADOS
            + "?useUnicode=true&characterEncoding=UTF-8&serverTimezone=America/Sao_Paulo"
            + "&useSSL=false&allowPublicKeyRetrieval=true";

    private ConexaoDB() {
        // Construtor privado: classe utilitária, não deve ser instanciada.
    }

    // Abre e devolve uma nova conexão com o banco de dados.
    public static Connection getConnection() throws SQLException {
    	
        try {
        
        	return DriverManager.getConnection(URL, USUARIO, SENHA);
        
        } catch (SQLException e) {
        
        	throw new SQLException("Falha ao conectar ao banco de dados '" + BANCO_DE_DADOS + "' em " + IPv4_SERVIDOR + ":" + PORTA + " -> " + e.getMessage(), e);
        	
        }
    }

    // Fecha a conexão de forma segura, sem propagar exceção para quem chamou.
    public static void closeConnection(Connection connection) {
 
       if (connection != null) {

            try {

                connection.close();

            } catch (SQLException e) {

                System.err.println("Aviso: não foi possível fechar a conexão -> " + e.getMessage());

            }
       }
    }
}
