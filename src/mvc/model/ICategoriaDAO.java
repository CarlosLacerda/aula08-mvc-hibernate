package mvc.model;

import java.sql.SQLException;
import java.util.List;

/**
 * 
 * Interface para a classe CategoriaDAO
 * 
 */

public interface ICategoriaDAO {
	
    void Cadastrar(Categoria categoria) throws SQLException;
    void Atualizar(Categoria categoria) throws SQLException;
    void Excluir(int idCategoria) throws SQLException;
    List<Categoria> Consultar() throws SQLException;
    
}