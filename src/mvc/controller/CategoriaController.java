package mvc.controller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import mvc.model.CatalogoModel;
import mvc.model.Categoria;
import mvc.view.PainelCategorias;

/**
 * 
 * CONTROLADORA - trata os eventos da tela de categoria (cliques nos
 * botões) e decide quem deve agir: pede ao MODELO para gravar/excluir e
 * pede à VISÃO para limpar o formulário ou exibir mensagens.
 * 
 */

public class CategoriaController {

    private final CatalogoModel modelo;
    private final PainelCategorias visao;

    public CategoriaController(CatalogoModel modelo, PainelCategorias visao) {
        this.modelo = modelo;
        this.visao = visao;

        visao.addCadastrarListener(e -> cadastrar());
        visao.addAtualizarListener(e -> atualizar());
        visao.addExcluirListener(e -> excluir());
       
        // Associa a ação de desselecionar ao método de limpar formulário
        visao.addDesselecionarListener(e -> visao.limparFormulario());
    }
    
    // Apenas INSERE um novo registro (força id_categoria = 0)
    private void cadastrar() {
    	int id_categoria = visao.getIdEmEdicao();
        if (id_categoria != 0) {
            visao.mostrarAviso("Para atualizar uma categoria já existente, clique no botão 'Atualizar'.");
            return;
        }
    	
        try {
        	
            Categoria c = new Categoria(0, visao.getNome());
            modelo.salvarCategoria(c);
            visao.limparFormulario();
            
        } catch (IllegalArgumentException ex) {
        	
            visao.mostrarAviso(ex.getMessage());
            
        } catch (SQLIntegrityConstraintViolationException ex) {
        	
            visao.mostrarAviso("Já existe uma categoria com esse nome.");
            
        } catch (SQLException ex) {
        	
            visao.mostrarErro("Erro no banco de dados: " + ex.getMessage());
            
        }
    }

    private void atualizar() {
    	
    	int id_categoria = visao.getIdEmEdicao();
        
        if (id_categoria == 0) {
            visao.mostrarAviso("Para inserir um nova categoria, clique no botão 'Cadastrar'.");
            return;
        }
        
        try {
        	
            Categoria c = new Categoria(visao.getIdEmEdicao(), visao.getNome());
            modelo.salvarCategoria(c);
            visao.limparFormulario();
            
        } catch (IllegalArgumentException ex) {          // regra de negócio violada
        	
            visao.mostrarAviso(ex.getMessage());
            
        } catch (SQLIntegrityConstraintViolationException ex) {
        	
            visao.mostrarAviso("Já existe uma categoria com esse nome.");
            
        } catch (SQLException ex) {
        	
            visao.mostrarErro("Erro no banco de dados: " + ex.getMessage());
            
        }
    }

    private void excluir() {
        int id = visao.getIdEmEdicao();
        
        if (id == 0) {
            visao.mostrarAviso("Selecione uma categoria na tabela para excluir.");
            return;
        }
        
        if (!visao.confirmar("Deseja realmente excluir a categoria selecionada?")) {
            return;
        }
        
        try {
        	
            modelo.excluirCategoria(id);
            visao.limparFormulario();
            
        } catch (SQLIntegrityConstraintViolationException ex) {
        	
            visao.mostrarAviso("Esta categoria possui produtos e não pode ser excluída.");
            
        } catch (SQLException ex) {
        	
            visao.mostrarErro("Erro no banco de dados: " + ex.getMessage());
            
        }
    }
}