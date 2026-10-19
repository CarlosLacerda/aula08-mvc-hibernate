-- Script de criação do banco usado no exemplo (MariaDB)
CREATE DATABASE IF NOT EXISTS aula08
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE aula08;

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria   INT AUTO_INCREMENT PRIMARY KEY,
    nome_categoria VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS produto (
    id_produto     INT AUTO_INCREMENT PRIMARY KEY,
    nome_produto   VARCHAR(120) NOT NULL,
    preco          DECIMAL(10,2) NOT NULL,
    estoque        INT NOT NULL,
    id_categoria   INT NOT NULL,
    CONSTRAINT fk_produto_categoria
        FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria)
        ON DELETE RESTRICT
);

-- Dados de exemplo (opcional)
INSERT INTO categoria (nome_categoria) VALUES ('Eletrônicos'), ('Livros');
INSERT INTO produto (nome_produto, preco, estoque, id_categoria)
VALUES ('Fone de ouvido', 99.90, 10, 1),
       ('Livro de Java', 59.50, 5, 2);
