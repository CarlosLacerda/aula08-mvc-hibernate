# Catálogo de Produtos — MVC + Hibernate

Aplicação desktop em **Java Swing** com o padrão **MVC** para cadastrar **categorias** e **produtos**, com persistência em **MariaDB** usando **Hibernate (JPA)**.

Este projeto é a adaptação de um exemplo de aula que acessava o banco com **JDBC puro** (DAO com `PreparedStatement`). Aqui, o acesso a dados foi migrado para o Hibernate, mantendo a camada de visão, as controladoras e o modelo praticamente intactos.

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Hibernate ORM | 6.6.4.Final |
| Driver JDBC MariaDB | 3.5.1 |
| MariaDB | 12.x (testado com 12.3) |
| Maven | gerenciamento de dependências |
| Swing | interface gráfica |

## Funcionalidades

- Cadastrar, atualizar e excluir **categorias**.
- Cadastrar, atualizar e excluir **produtos**, cada um vinculado a uma categoria.
- Validações de regra de negócio (nome obrigatório, preço e estoque não negativos).
- Avisos amigáveis para nome de categoria repetido e para exclusão de categoria que possui produtos.
- A tela se atualiza automaticamente quando o modelo muda (padrão Observer com `PropertyChangeSupport`).

## Estrutura do projeto

```
.
├── pom.xml
├── sql/
│   └── aula08.sql                 # criação do banco e das tabelas
└── src/
    ├── hibernate.cfg.xml          # configuração de conexão e entidades
    └── mvc/
        ├── Main.java              # monta modelo, visão e controladoras
        ├── controller/            # CategoriaController, ProdutoController
        ├── view/                  # JanelaPrincipal, PainelCategorias, PainelProdutos
        └── model/
            ├── Categoria.java     # entidade
            ├── Produto.java       # entidade
            ├── ICategoriaDAO.java / IProdutoDAO.java
            ├── CategoriaDAO.java / ProdutoDAO.java   # acesso a dados com Hibernate
            ├── HibernateUtil.java # SessionFactory, transações, tradução de exceções
            └── CatalogoModel.java # fachada do modelo + regras de negócio
```

## Como executar

### 1. Pré-requisitos

- JDK 21 (ou 17, ajustando `maven.compiler.release` no `pom.xml`)
- MariaDB em execução
- Eclipse com suporte a Maven (m2e) ou outra IDE com Maven

### 2. Criar o banco

Execute o script [`sql/aula08.sql`](sql/aula08.sql) no seu cliente SQL (HeidiSQL, DBeaver, terminal etc.). Ele cria o banco `aula08`, as tabelas `categoria` e `produto` e alguns dados de exemplo.

### 3. Configurar a conexão

Edite `src/hibernate.cfg.xml` com os dados do seu MariaDB:

```xml
<property name="jakarta.persistence.jdbc.url">jdbc:mariadb://localhost:3306/aula08?...</property>
<property name="jakarta.persistence.jdbc.user">root</property>
<property name="jakarta.persistence.jdbc.password">SUA_SENHA</property>
```

A URL deve ficar em **uma única linha**.

> **Atenção:** não envie sua senha real para o GitHub. Mantenha o placeholder `SUA_SENHA` no repositório e altere o valor apenas na sua máquina.

### 4. Abrir e rodar

No Eclipse:

1. `File > Import > Maven > Existing Maven Projects` e selecione a pasta do projeto.
2. Aguarde o download das dependências.
3. Execute a classe `mvc.Main` (`Run As > Java Application`).

## Como funciona a camada de dados

As classes `Categoria` e `Produto` são **entidades** mapeadas por anotações JPA, e o Hibernate gera o SQL a partir delas:

```java
@Entity
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private int idCategoria;

    @Column(name = "nome_categoria", nullable = false, length = 80)
    private String nomeCategoria;
    ...
}
```

| Operação | Antes (JDBC) | Agora (Hibernate) |
|---|---|---|
| Consultar | `SELECT` + `ResultSet` | HQL: `from Categoria c order by c.idCategoria` |
| Cadastrar | `INSERT` + `getGeneratedKeys()` | `session.persist(objeto)` |
| Atualizar | `UPDATE` | `session.merge(objeto)` |
| Excluir | `DELETE` | `session.find(...)` + `session.remove(...)` |

Pontos de destaque:

- **`HibernateUtil`** cria a `SessionFactory` uma única vez e oferece `emTransacao(...)` (commit/rollback) e `consultar(...)` (somente leitura).
- As exceções do Hibernate são convertidas em `SQLException` / `SQLIntegrityConstraintViolationException`, então as controladoras não precisaram mudar.
- O `ProdutoDAO` usa `join fetch p.categoria` para trazer a categoria junto com o produto, pois a associação é `LAZY`.
- `hibernate.hbm2ddl.auto = none`: o Hibernate **não** cria nem altera tabelas; o esquema vem do script SQL.
- `hibernate.show_sql = true`: o SQL gerado aparece no console (útil para estudar).

## Problemas comuns

| Sintoma | Solução |
|---|---|
| `Table '...categoria' doesn't exist` | Execute o `sql/aula08.sql` no banco configurado na URL. |
| `Access denied for user` | Confira usuário e senha no `hibernate.cfg.xml`. |
| `Unknown database` | O nome do banco na URL deve ser o mesmo criado pelo script (`aula08`). |
| `Communications link failure` | MariaDB desligado, host ou porta incorretos. |
| Imports `jakarta.persistence` em vermelho | `Maven > Update Project` com *Force Update* e internet ligada. |
| `release version 21 not supported` | Use JDK 21 ou mude `maven.compiler.release` para `17` no `pom.xml`. |
| Aviso `SLF4J: No SLF4J providers were found` | Normal; pode ser ignorado. |

## Autor

Seu Nome — [Carlos Lacerda](https://github.com/CarlosLacerda)

Projeto desenvolvido para fins didáticos, a partir do exemplo da *Aula 08*.
