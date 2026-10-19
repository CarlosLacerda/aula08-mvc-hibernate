package mvc;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import mvc.controller.CategoriaController;
import mvc.controller.ProdutoController;
import mvc.model.CatalogoModel;
import mvc.view.JanelaPrincipal;



public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignorada) {
            }

            CatalogoModel modelo = new CatalogoModel();
            JanelaPrincipal janela = new JanelaPrincipal();

            modelo.addPropertyChangeListener(janela.getPainelCategorias());
            modelo.addPropertyChangeListener(janela.getPainelProdutos());

            new CategoriaController(modelo, janela.getPainelCategorias());
            new ProdutoController(modelo, janela.getPainelProdutos());

            janela.setVisible(true);

            try {
                modelo.carregarTudo();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(janela,
                        "Não foi possível conectar ao banco de dados.\n"
                        + "Verifique se o MariaDB está ativo, se os scripts SQL foram executados\n"
                        + "e se o hibernate.cfg.xml tem o IP e o RA corretos.\\n\\n"
                        + ex.getMessage(),
                        "Erro de conexão", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
