package mvc.model;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.exception.ConstraintViolationException;

public final class HibernateUtil {
	@FunctionalInterface
	public interface Operacao<T> {
		T executar(Session session);
	}

	private static SessionFactory fabrica;

	private HibernateUtil() {
	}

	private static synchronized SessionFactory getSessionFactory() {
		if (fabrica == null) {
			StandardServiceRegistry registro = new StandardServiceRegistryBuilder().configure().build();
			try {
				fabrica = new MetadataSources(registro).buildMetadata().buildSessionFactory();
			} catch (RuntimeException e) {
				StandardServiceRegistryBuilder.destroy(registro);
				throw e;
			}
			Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::fechar));
		}
		return fabrica;
	}

	public static <T> T emTransacao(Operacao<T> operacao) throws SQLException {
		try (Session session = getSessionFactory().openSession()) {
			Transaction tx = session.beginTransaction();
			try {
				T resultado = operacao.executar(session);
				tx.commit();
				return resultado;
			} catch (RuntimeException e) {
				if (tx.isActive()) {
					tx.rollback();
				}
				throw e;
			}
		} catch (RuntimeException e) {
			throw traduzir(e);
		}
	}

	public static <T> T consultar(Operacao<T> operacao) throws SQLException {
		try (Session session = getSessionFactory().openSession()) {
			return operacao.executar(session);
		} catch (RuntimeException e) {
			throw traduzir(e);
		}
	}

	public static synchronized void fechar() {
		if (fabrica != null && fabrica.isOpen()) {
			fabrica.close();
		}
		fabrica = null;
	}

	private static SQLException traduzir(Throwable erro) {
		for (Throwable t = erro; t != null; t = t.getCause()) {
			if (t instanceof SQLIntegrityConstraintViolationException) {
				return (SQLIntegrityConstraintViolationException) t;
			}
			if (t instanceof ConstraintViolationException) {
				SQLException origem = ((ConstraintViolationException) t).getSQLException();
				return new SQLIntegrityConstraintViolationException(origem.getMessage(), origem.getSQLState(),
						origem.getErrorCode(), t);
			}
		}
		for (Throwable t = erro; t != null; t = t.getCause()) {
			if (t instanceof SQLException) {
				return (SQLException) t;
			}
		}
		return new SQLException("Falha ao acessar o banco de dados via Hibernate: " + erro.getMessage(), erro);
	}
}