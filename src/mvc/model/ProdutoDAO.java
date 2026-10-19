package mvc.model;

import java.sql.SQLException;
import java.util.List;

public class ProdutoDAO implements IProdutoDAO {
	public List<Produto> Consultar() throws SQLException {
		return HibernateUtil.consultar(session -> session
				.createQuery("select p from Produto p join fetch p.categoria order by p.idProduto", Produto.class)
				.getResultList());
	}

	public void Cadastrar(Produto p) throws SQLException {
		HibernateUtil.emTransacao(session -> {
			session.persist(p);
			return null;
		});
	}

	public void Atualizar(Produto p) throws SQLException {
		HibernateUtil.emTransacao(session -> session.merge(p));
	}

	public void Excluir(int id) throws SQLException {
		HibernateUtil.emTransacao(session -> {
			Produto p = session.find(Produto.class, id);
			if (p != null) {
				session.remove(p);
			}
			return null;
		});
	}
}