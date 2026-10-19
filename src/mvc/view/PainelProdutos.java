package mvc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import mvc.model.CatalogoModel;
import mvc.model.Categoria;
import mvc.model.Produto;

/**
 * 
 * VISÃO - tela de cadastro de produtos.
 * Observa o modelo: atualiza a tabela quando "produtos" muda e a lista
 * suspensa (combo) quando "categorias" muda.
 * 
 */

public class PainelProdutos extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    private final JTextField txtNome = new JTextField(22);
    private final JTextField txtPreco = new JTextField(8);
    private final JTextField txtEstoque = new JTextField(6);
    private final JComboBox<Categoria> cmbCategoria = new JComboBox<>();
    private final JButton btnCadastrar = new JButton("Cadastrar");
    private final JButton btnAtualizar = new JButton("Atualizar");
    private final JButton btnExcluir = new JButton("Excluir");
    private final JButton btnDesselecionar = new JButton("Desselecionar"); // Novo botão

    private final DefaultTableModel tabelaModelo = new DefaultTableModel(new Object[] { "ID", "Nome", "Preço", "Estoque", "Categoria" }, 0) {
    	private static final long serialVersionUID = 1L;
    	
        @Override
        public boolean isCellEditable(int linha, int coluna) {
        	return false;
        }
    };
    
    private final JTable tabela = new JTable(tabelaModelo);
    private List<Produto> exibidos = new ArrayList<>();
    private int idEmEdicao = 0;   // 0 = novo produto

    public PainelProdutos() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Nome:"));
        formulario.add(txtNome);
        formulario.add(new JLabel("Preço (R$):"));
        formulario.add(txtPreco);
        formulario.add(new JLabel("Estoque:"));
        formulario.add(txtEstoque);
        formulario.add(new JLabel("Categoria:"));
        formulario.add(cmbCategoria);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Oculta o botão '' inicialmente
        btnDesselecionar.setVisible(false);
        botoes.add(btnDesselecionar);       
        botoes.add(btnCadastrar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setMaxWidth(60);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);
       
        // Atualiza o ouvinte de seleção da tabela:
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int linha = tabela.getSelectedRow();
            if (linha >= 0 && linha < exibidos.size()) {
                Produto p = exibidos.get(linha);
                idEmEdicao = p.getIdProduto();
                txtNome.setText(p.getNomeProduto());
                txtPreco.setText(p.getPreco().toPlainString());
                txtEstoque.setText(String.valueOf(p.getEstoque()));
                selecionarCategoria(p.getCategoria().getIdCategoria());

                // Exibe o botão Desselecionar ao selecionar uma linha
                btnDesselecionar.setVisible(true);
            } else {
                // Esconde se nada estiver selecionado
                btnDesselecionar.setVisible(false);
            }
        });
        
    }

    // OBSERVADORA do modelo
    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        String nome = evt.getPropertyName();

        if (CatalogoModel.CATEGORIAS.equals(nome)) {
            Categoria anterior = getCategoriaSelecionada();
            cmbCategoria.removeAllItems();
            cmbCategoria.addItem(new Categoria(0, "Selecione..."));
            
            for (Categoria c : (List<Categoria>) evt.getNewValue()) {
                cmbCategoria.addItem(c);
            }
            
            if (anterior != null && anterior.getIdCategoria() != 0) {
                selecionarCategoria(anterior.getIdCategoria());
            } else {
            	cmbCategoria.setSelectedIndex(0); // Aponta para "Selecione..." por padrão
            }

        } else if (CatalogoModel.PRODUTOS.equals(nome)) {
            exibidos = new ArrayList<>((List<Produto>) evt.getNewValue());
            tabelaModelo.setRowCount(0);
            for (Produto p : exibidos) {
                tabelaModelo.addRow(new Object[] {
                        p.getIdProduto(),
                        p.getNomeProduto(),
                        MOEDA.format(p.getPreco()),
                        p.getEstoque(),
                        p.getCategoria().getNomeCategoria() });
            }
        }
    }

    private void selecionarCategoria(int id) {
        for (int i = 0; i < cmbCategoria.getItemCount(); i++) {
            if (cmbCategoria.getItemAt(i).getIdCategoria() == id) {
                cmbCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    // Dados digitados
    public int getIdEmEdicao() { return idEmEdicao; }
    public String getNomeProduto() { return txtNome.getText(); }
    public String getPrecoDigitado() { return txtPreco.getText(); }
    public String getEstoqueDigitado() { return txtEstoque.getText(); }
    public Categoria getCategoriaSelecionada() { return (Categoria) cmbCategoria.getSelectedItem(); }

/*
    // Ações da tela
    public void limparFormulario() {
        idEmEdicao = 0;
        txtNome.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
        tabela.clearSelection();
        txtNome.requestFocusInWindow();
    }
*/
    
    // 4. Atualize o método limparFormulario() para esconder o botão ao limpar a seleção:
    public void limparFormulario() {
        idEmEdicao = 0;
        txtNome.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }
        tabela.clearSelection();
        btnDesselecionar.setVisible(false); // Esconde o botão
        txtNome.requestFocusInWindow();
    }

    // 5. Adicione o método para conectar o listener no controlador:
    public void addDesselecionarListener(ActionListener l) {
        btnDesselecionar.addActionListener(l);
    }    
    
    public void mostrarAviso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmar(String pergunta) {
        return JOptionPane.showConfirmDialog(this, pergunta, "Confirmação",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // Ligação com o controlador
    public void addCadastrarListener(ActionListener l) { btnCadastrar.addActionListener(l); }
    public void addAtualizarListener(ActionListener l) { btnAtualizar.addActionListener(l); }
    public void addExcluirListener(ActionListener l) { btnExcluir.addActionListener(l); }
}
