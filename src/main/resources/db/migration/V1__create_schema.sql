-- Migration inicial: Criação do schema do Armário Inteligente

CREATE TABLE tipo_usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL UNIQUE,
    descricao VARCHAR(500)
);

CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE armario (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    numero VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL,
    localizacao VARCHAR(255) NOT NULL
);

CREATE TABLE encomenda (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo_rastreio VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(500) NOT NULL,
    remetente VARCHAR(100) NOT NULL,
    data_recebimento TIMESTAMP NOT NULL,
    data_retirada TIMESTAMP,
    retirada_confirmada BOOLEAN NOT NULL DEFAULT FALSE,
    id_armario UUID NOT NULL,
    id_usuario UUID NOT NULL,
    CONSTRAINT fk_encomenda_armario FOREIGN KEY (id_armario) REFERENCES armario(id),
    CONSTRAINT fk_encomenda_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE compartimento (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    numero VARCHAR(255) NOT NULL,
    tamanho DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    id_armario UUID NOT NULL,
    CONSTRAINT fk_compartimento_armario FOREIGN KEY (id_armario) REFERENCES armario(id)
);

CREATE TABLE notificacao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(100) NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    tipo_notificacao VARCHAR(50) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_leitura TIMESTAMP,
    lida BOOLEAN NOT NULL DEFAULT FALSE,
    id_usuario UUID NOT NULL,
    CONSTRAINT fk_notificacao_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE registro_auditoria (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    acao VARCHAR(255) NOT NULL,
    detalhes VARCHAR(500),
    data_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Adiciona a foreign key circular após criar todas as tabelas
ALTER TABLE armario
    ADD COLUMN id_encomenda_atual UUID,
    ADD CONSTRAINT fk_armario_encomenda FOREIGN KEY (id_encomenda_atual) REFERENCES encomenda(id);

-- Cria índices para melhorar a performance
CREATE INDEX idx_encomenda_armario ON encomenda(id_armario);
CREATE INDEX idx_encomenda_usuario ON encomenda(id_usuario);
CREATE INDEX idx_encomenda_codigo_rastreio ON encomenda(codigo_rastreio);
CREATE INDEX idx_compartimento_armario ON compartimento(id_armario);
CREATE INDEX idx_notificacao_usuario ON notificacao(id_usuario);
CREATE INDEX idx_notificacao_lida ON notificacao(lida); 