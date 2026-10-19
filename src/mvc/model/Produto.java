package mvc.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "produto")
public class Produto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_produto")
	private int idProduto; 
	@Column(name = "nome_produto", nullable = false, length = 120)
	private String nomeProduto;
	@Column(name = "preco", nullable = false)
	private BigDecimal preco;
	@Column(name = "estoque", nullable = false)
	private int estoque;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "id_categoria", nullable = false)
	private Categoria categoria;

	protected Produto() {
	}

	public Produto(int idProduto, String nomeProduto, BigDecimal preco, int estoque, Categoria categoria) {
		this.idProduto = idProduto;
		this.nomeProduto = nomeProduto;
		this.preco = preco;
		this.estoque = estoque;
		this.categoria = categoria;
	}

	public int getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public int getEstoque() {
		return estoque;
	}

	public void setEstoque(int estoque) {
		this.estoque = estoque;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}
