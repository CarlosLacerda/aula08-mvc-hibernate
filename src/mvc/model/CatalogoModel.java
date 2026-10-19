package mvc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * MODELO (fachada do modelo) - guarda os dados da aplicação, aplica as
 * regras de negócio e AVISA os observadores quando algo muda.
 *
 * Esta classe NÃO conhece a visão nem os controladores: ela só dispara
 * eventos (PropertyChangeEvent). Quem quiser saber das mudanças (a
 * interface gráfica) se registra como observador - é o que o slide chama
 * de "a interface gráfica é uma OBSERVADORA do modelo".
 * 
 */

public class CatalogoModel {

    public static final String CATEGORIAS = "categorias";
    public static final String PRODUTOS = "produtos";
    private final PropertyChangeSupport observadores = new PropertyChangeSupport(this);
    private final ICategoriaDAO categoriaDAO = new CategoriaDAO();
    private final IProdutoDAO produtoDAO = new ProdutoDAO();
    private List<Categoria> categorias = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();

    // ------------------------------------------------------------ observadores
    public void addPropertyChangeListener(PropertyChangeListener l) {
        observadores.addPropertyChangeListener(l);
    }

    // ------------------------------------------------------------ consultas
    public List<Categoria> getCategorias() {
        return Collections.unmodifiableList(categorias);
    }

    public List<Produto> getProdutos() {
        return Collections.unmodifiableList(produtos);
    }

    // ------------------------------------------------------------ carga
    public void carregarTudo() throws SQLException {
        carregarCategorias();
        carregarProdutos();
    }

    public void carregarCategorias() throws SQLException {
        categorias = categoriaDAO.Consultar();
        // old = null para garantir que o evento sempre seja disparado
        observadores.firePropertyChange(CATEGORIAS, null, getCategorias());
    }

    public void carregarProdutos() throws SQLException {
        produtos = produtoDAO.Consultar();
        observadores.firePropertyChange(PRODUTOS, null, getProdutos());
    }

    // ------------------------------------------------------------ categorias
    public void salvarCategoria(Categoria categoria) throws SQLException {
    	
        if (categoria.getNomeCategoria() == null || categoria.getNomeCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }
        
        if (categoria.getNomeCategoria().trim().length() > 80) {
            throw new IllegalArgumentException("O nome da categoria deve ter no máximo 80 caracteres.");
        }
        
        categoria.setNomeCategoria(categoria.getNomeCategoria().trim());

        if (categoria.getIdCategoria() == 0) {
            categoriaDAO.Cadastrar(categoria);
        } else {
            categoriaDAO.Atualizar(categoria);
        }
        
        // O nome da categoria aparece na lista de produtos (JOIN): recarrega os dois.
        carregarTudo();
    }

    public void excluirCategoria(int idCategoria) throws SQLException {
        categoriaDAO.Excluir(idCategoria);   // falha se houver produtos (FK RESTRICT)
        carregarTudo();
    }

    // ------------------------------------------------------------ produtos
    public void salvarProduto(Produto produto) throws SQLException {
        if (produto.getNomeProduto() == null || produto.getNomeProduto().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (produto.getNomeProduto().trim().length() > 120) {
            throw new IllegalArgumentException("O nome do produto deve ter no máximo 120 caracteres.");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        if (produto.getEstoque() < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }
        if (produto.getCategoria() == null) {
            throw new IllegalArgumentException("Selecione uma categoria.");
        }
        produto.setNomeProduto(produto.getNomeProduto().trim());

        if (produto.getIdProduto() == 0) {
            produtoDAO.Cadastrar(produto);
        } else {
            produtoDAO.Atualizar(produto);
        }
        carregarProdutos();
    }

    public void excluirProduto(int id) throws SQLException {
        produtoDAO.Excluir(id);
        carregarProdutos();
    }
}
