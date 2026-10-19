package mvc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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

/**
 * 
 * VISÃO - tela de cadastro de categorias.
 *
 * A visão só sabe DESENHAR a tela e EXPOR seus componentes. Ela não grava
 * nada no banco e não decide o que fazer quando um botão é clicado: isso é
 * papel do CategoriaController. Ela é OBSERVADORA do modelo: quando o
 * CatalogoModel avisa que as categorias mudaram, a tabela é redesenhada.
 * 
 */

public class PainelCategorias extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;
    private final JTextField txtNome = new JTextField(30);
    private final JButton btnCadastrar = new JButton("Cadastrar");
    private final JButton btnAtualizar = new JButton("Atualizar");
    private final JButton btnExcluir = new JButton("Excluir");
    private final JButton btnDesselecionar = new JButton("Desselecionar"); // Novo botão

    private final DefaultTableModel tabelaModelo = new DefaultTableModel(new Object[] { "ID", "Nome" }, 0) {
    	private static final long serialVersionUID = 1L;
    	
        @Override
        public boolean isCellEditable(int linha, int coluna) {
        	return false;
        }
	};
	
    private final JTable tabela = new JTable(tabelaModelo);
    private List<Categoria> exibidas = new ArrayList<>();
    private int idEmEdicao = 0;   // 0 = nova categoria

    public PainelCategorias() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Nome:"));
        formulario.add(txtNome);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
     // Oculta o botão inicialmente
        btnDesselecionar.setVisible(false);
        botoes.add(btnDesselecionar);
        botoes.add(btnCadastrar);
        botoes.add(btnAtualizar);
        botoes.add(btnExcluir);

        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setMaxWidth(80);

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        // Ao selecionar uma linha, preenche o formulário para edição
        tabela.getSelectionModel().addListSelectionListener(e -> {
            
        	if (e.getValueIsAdjusting()) {
                return;
            }
            
            int linha = tabela.getSelectedRow();
            
            if (linha >= 0 && linha < exibidas.size()) {
            	
                Categoria categoria = exibidas.get(linha);
                idEmEdicao = categoria.getIdCategoria();
                txtNome.setText(categoria.getNomeCategoria());
                
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
        if (CatalogoModel.CATEGORIAS.equals(evt.getPropertyName())) {
            exibidas = new ArrayList<>((List<Categoria>) evt.getNewValue());
            tabelaModelo.setRowCount(0);
            
            for (Categoria categoria : exibidas) {
                tabelaModelo.addRow(new Object[] { categoria.getIdCategoria(), categoria.getNomeCategoria() });
            }
        }
    }

    // Dados digitados
    public int getIdEmEdicao() {
        return idEmEdicao;
    }

    public String getNome() {
        return txtNome.getText();
    }

    // Ações da tela
    public void limparFormulario() {
        idEmEdicao = 0;
        txtNome.setText("");
        tabela.clearSelection();
        btnDesselecionar.setVisible(false); // Esconde o botão
        txtNome.requestFocusInWindow();
    }
    
    // Adiciona o método para conectar o listener no controlador:
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
        return JOptionPane.showConfirmDialog(this, pergunta, "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // Ligação com o controlador
    public void addCadastrarListener(ActionListener l) { btnCadastrar.addActionListener(l); }
    public void addAtualizarListener(ActionListener l) { btnAtualizar.addActionListener(l); }
    public void addExcluirListener(ActionListener l) { btnExcluir.addActionListener(l); }
}
