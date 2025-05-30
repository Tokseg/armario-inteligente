-- Seeder inicial: Populando tabelas do Armário Inteligente

-- Tipos de usuário
INSERT INTO tipo_usuario (nome, descricao) VALUES ('Morador', 'Usuário morador do condomínio');
INSERT INTO tipo_usuario (nome, descricao) VALUES ('Porteiro', 'Responsável pela portaria');
INSERT INTO tipo_usuario (nome, descricao) VALUES ('Administrador', 'Administrador do sistema');

-- Usuários
INSERT INTO usuarios (nome, email, senha, telefone, tipo_usuario_id) VALUES ('João Silva', 'joao@exemplo.com', 'senha123', '11999999999', 1);
INSERT INTO usuarios (nome, email, senha, telefone, tipo_usuario_id) VALUES ('Maria Souza', 'maria@exemplo.com', 'senha456', '11888888888', 1);
INSERT INTO usuarios (nome, email, senha, telefone, tipo_usuario_id) VALUES ('Carlos Porteiro', 'carlos@exemplo.com', 'porteiro', '11777777777', 2);

-- Armários
INSERT INTO armario (ocupado) VALUES (false);
INSERT INTO armario (ocupado) VALUES (false);

-- Encomendas
INSERT INTO encomenda (id_encomenda, descricao, remetente, data_recebimento, id_armario, id_usuario) VALUES ('E001', 'Caixa Amazon', 'Amazon', CURRENT_TIMESTAMP, 1, 1);
INSERT INTO encomenda (id_encomenda, descricao, remetente, data_recebimento, id_armario, id_usuario) VALUES ('E002', 'Envelope Mercado Livre', 'Mercado Livre', CURRENT_TIMESTAMP, 2, 2);

-- Compartimentos
INSERT INTO compartimento (id_armario, ocupado, id_encomenda_atual) VALUES (1, true, 'E001');
INSERT INTO compartimento (id_armario, ocupado, id_encomenda_atual) VALUES (2, true, 'E002');

-- Notificações
INSERT INTO notificacao (id_notificacao, id_usuario, mensagem, data_envio, lida) VALUES ('N001', 1, 'Sua encomenda chegou!', CURRENT_TIMESTAMP, false);
INSERT INTO notificacao (id_notificacao, id_usuario, mensagem, data_envio, lida) VALUES ('N002', 2, 'Sua encomenda chegou!', CURRENT_TIMESTAMP, false);

-- Auditoria
INSERT INTO registro_auditoria (acao, detalhes, data_hora) VALUES ('Cadastro de Encomenda', 'Encomenda E001 cadastrada para João Silva', CURRENT_TIMESTAMP);
INSERT INTO registro_auditoria (acao, detalhes, data_hora) VALUES ('Cadastro de Encomenda', 'Encomenda E002 cadastrada para Maria Souza', CURRENT_TIMESTAMP);
