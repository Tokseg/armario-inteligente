-- Migration inicial: Criação do schema do Armário Inteligente

CREATE TABLE tipo_usuario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL UNIQUE,
    descricao VARCHAR(500)
);

CREATE TABLE usuarios (
    id_usuario BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(255) NOT NULL,
    tipo_usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario_tipo FOREIGN KEY (tipo_usuario_id) REFERENCES tipo_usuario(id)
);

CREATE TABLE armario (
    id_armario BIGINT PRIMARY KEY AUTO_INCREMENT,
    ocupado BOOLEAN NOT NULL,
    id_encomenda_atual VARCHAR(255)
);

CREATE TABLE encomenda (
    id_encomenda VARCHAR(255) PRIMARY KEY,
    descricao VARCHAR(255),
    remetente VARCHAR(255),
    data_recebimento TIMESTAMP,
    id_armario BIGINT,
    id_usuario BIGINT,
    CONSTRAINT fk_encomenda_armario FOREIGN KEY (id_armario) REFERENCES armario(id_armario),
    CONSTRAINT fk_encomenda_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE compartimento (
    id_compartimento BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_armario BIGINT,
    ocupado BOOLEAN NOT NULL,
    id_encomenda_atual VARCHAR(255),
    CONSTRAINT fk_compartimento_armario FOREIGN KEY (id_armario) REFERENCES armario(id_armario),
    CONSTRAINT fk_compartimento_encomenda FOREIGN KEY (id_encomenda_atual) REFERENCES encomenda(id_encomenda)
);

CREATE TABLE notificacao (
    id_notificacao VARCHAR(255) PRIMARY KEY,
    id_usuario BIGINT,
    mensagem VARCHAR(255),
    data_envio TIMESTAMP,
    CONSTRAINT fk_notificacao_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

CREATE TABLE registro_auditoria (
    id_registro BIGINT PRIMARY KEY AUTO_INCREMENT,
    acao VARCHAR(255) NOT NULL,
    detalhes VARCHAR(255),
    data_hora TIMESTAMP NOT NULL
);

-- Agora adicione a foreign key circular
ALTER TABLE armario
ADD CONSTRAINT fk_armario_encomenda FOREIGN KEY (id_encomenda_atual) REFERENCES encomenda(id_encomenda); 