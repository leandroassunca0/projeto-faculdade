-- 1. Tabela de Tipos de Pessoa (ex: Estagiário, CLT)
CREATE TABLE tb_pessoa_tipo (
    pessoa_tipo_id SERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL UNIQUE
);

-- 2. Tabela de Usuários do Sistema (para login)
CREATE TABLE tb_usuarios (
    usuario_id SERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por INT REFERENCES tb_usuarios(usuario_id)
);

-- 3. Tabela de Pessoas
CREATE TABLE tb_pessoas (
    pessoa_id SERIAL PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    nascimento DATE NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    pessoa_tipo_id INT NOT NULL REFERENCES tb_pessoa_tipo(pessoa_tipo_id),
    atualizado_por INT REFERENCES tb_usuarios(usuario_id)
);

-- 4. Tabela de Registro de Ponto
CREATE TABLE tb_ponto (
    ponto_id SERIAL PRIMARY KEY,
    inicio TIMESTAMP NOT NULL,
    saida TIMESTAMP,
    justificativa VARCHAR(255),
    funcionario_id INT NOT NULL REFERENCES tb_pessoas(pessoa_id),
    atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_por INT REFERENCES tb_usuarios(usuario_id)
);