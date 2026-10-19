package mvc.view;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * 
 * VISÃO - janela principal com uma aba para cada cadastro.
 * 
 */

public class JanelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;
    private final PainelProdutos painelProdutos = new PainelProdutos();
    private final PainelCategorias painelCategorias = new PainelCategorias();

    public JanelaPrincipal() {
        super("Aula 08 - Exemplo (Aplicação Tradicional - JAVA)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Produtos", painelProdutos);
        abas.addTab("Categorias", painelCategorias);
        add(abas);
        setSize(1000, 500);
        setLocationRelativeTo(null);
    }

    public PainelProdutos getPainelProdutos() { return painelProdutos; }
    public PainelCategorias getPainelCategorias() { return painelCategorias; }
}
