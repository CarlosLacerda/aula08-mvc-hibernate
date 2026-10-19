package mvc.controller;

import mvc.model.CatalogoModel;
import mvc.model.Categoria;
import mvc.model.Produto;
import mvc.view.PainelProdutos;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 
 * CONTROLADORA - trata os eventos da tela de produto (cliques nos
 * botões) e decide quem deve agir: pede ao MODELO para gravar/excluir e
 * pede à VISÃO para limpar o formulário ou exibir mensagens.
 * 
 */

public class ProdutoController {

    private final CatalogoModel modelo;
    private final PainelProdutos visao;

    public ProdutoController(CatalogoModel modelo, PainelProdutos visao) {
        this.modelo = modelo;
        this.visao = visao;

        // SEPARAÇÃO DAS AÇÕES DOS BOTÕES:
        this.visao.addCadastrarListener(e -> cadastrar());
        this.visao.addAtualizarListener(e -> atualizar());
        this.visao.addExcluirListener(e -> excluir());
        
        // Associa a ação de desselecionar ao método de limpar formulário
        this.visao.addDesselecionarListener(e -> visao.limparFormulario());
    }

    // Apenas insere novos produtos
    private void cadastrar() {
    	int id_produto = visao.getIdEmEdicao();
        if (id_produto != 0) {
            visao.mostrarAviso("Para atualizar um produto já existente, clique no botão 'Atualizar'.");
            return;
        }
        salvar(0); // Passa ID 0 para forçar criação
    }

    // Apenas atualiza produtos existentes
    private void atualizar() {
        int id_produto = visao.getIdEmEdicao();
        if (id_produto == 0) {
            visao.mostrarAviso("Para inserir um novo produto, clique no botão 'Cadastrar'.");
            return;
        }
        salvar(id_produto);
    }

    private void salvar(int id_produto) {
        try {
            Categoria categoria = visao.getCategoriaSelecionada();
            String nome_produto = visao.getNomeProduto().trim(); // Se a sua tela usar getNomeProduto(), altere para visao.getNomeProduto()
            String precoStr = visao.getPrecoDigitado().replace(",", ".").trim();
            String estoqueStr = visao.getEstoqueDigitado().trim();
           
            // Bloqueia caso o aviso "Selecione..." esteja ativo
            if (categoria == null || categoria.getIdCategoria() == 0) {
                visao.mostrarAviso("Por favor, selecione uma categoria válida.");
                return;
            }
            
            if (nome_produto.isEmpty() || precoStr.isEmpty() || estoqueStr.isEmpty()) {
                visao.mostrarAviso("Preencha todos os campos do formulário.");
                return;
            }

            BigDecimal preco = new BigDecimal(precoStr.replace(",", "."));
            int estoque = Integer.parseInt(estoqueStr);

            // CORREÇÃO: Passando parâmetros na ordem exata do construtor Produto
            Produto p = new Produto(id_produto, nome_produto, preco, estoque, categoria);
            
            modelo.salvarProduto(p);
            visao.limparFormulario();

        } catch (NumberFormatException ex) {
            visao.mostrarAviso("Preço e Estoque devem conter valores numéricos válidos.");
        } catch (IllegalArgumentException ex) {
            visao.mostrarAviso(ex.getMessage());
        } catch (SQLIntegrityConstraintViolationException ex) {
            visao.mostrarAviso("Erro de integridade no banco de dados.");
        } catch (SQLException ex) {
            visao.mostrarErro("Erro no banco de dados: " + ex.getMessage());
        }
    }

    private void excluir() {
        int id = visao.getIdEmEdicao();
        if (id == 0) {
            visao.mostrarAviso("Selecione um produto na tabela para excluir.");
            return;
        }
        if (!visao.confirmar("Deseja realmente excluir o produto selecionado?")) {
            return;
        }
        try {
            modelo.excluirProduto(id);
            visao.limparFormulario();
        } catch (SQLException ex) {
            visao.mostrarErro("Erro no banco de dados: " + ex.getMessage());
        }
    }
}