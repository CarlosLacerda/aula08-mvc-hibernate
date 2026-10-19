package mvc.model;

import java.sql.SQLException;
import java.util.List;

/**
 * 
 * Interface para a classe ProdutoDAO
 * 
 */

public interface IProdutoDAO {
	
    void Cadastrar(Produto Produto) throws SQLException;
    void Atualizar(Produto Produto) throws SQLException;
    void Excluir(int idProduto) throws SQLException;
    List<Produto> Consultar() throws SQLException;
    
}
