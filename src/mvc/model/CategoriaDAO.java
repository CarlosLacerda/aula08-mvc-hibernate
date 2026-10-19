package mvc.model;

import java.sql.SQLException;
import java.util.List;

public class CategoriaDAO implements ICategoriaDAO {
	public List<Categoria> Consultar() throws SQLException {
		return HibernateUtil.consultar(session -> session
				.createQuery("from Categoria c order by c.idCategoria", Categoria.class).getResultList());
	}

	public void Cadastrar(Categoria categoria) throws SQLException {
		HibernateUtil.emTransacao(session -> {
			session.persist(categoria);
			return null;
		});
	}

	public void Atualizar(Categoria categoria) throws SQLException {
		HibernateUtil.emTransacao(session -> session.merge(categoria));
	}

	public void Excluir(int idCategoria) throws SQLException {
		HibernateUtil.emTransacao(session -> {
			Categoria c = session.find(Categoria.class, idCategoria);
			if (c != null) {
				session.remove(c); 
			}
			return null;
		});
	}
}