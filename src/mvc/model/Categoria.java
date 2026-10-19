package mvc.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categoria")
public class Categoria {
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    @Column(name = "id_categoria")
    
	private int idCategoria;          
    
	@Column(name = "nome_categoria", nullable = false, length = 80)
    
	private String nomeCategoria;
    
    protected Categoria() {
    }
    
    public Categoria(int idCategoria, String nomeCategoria) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
    }
    
    public int getIdCategoria() { return idCategoria; }
    
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    
    public String getNomeCategoria() { return nomeCategoria; }
    
    public void setNomeCategoria(String nomeCategoria) { this.nomeCategoria = nomeCategoria; }
    
    @Override
    public String toString() {
        return nomeCategoria;
    }
}