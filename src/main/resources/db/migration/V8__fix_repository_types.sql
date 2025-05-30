-- Atualiza o tipo do ID do registro_auditoria para INTEGER
ALTER TABLE registro_auditoria 
    ALTER COLUMN id_registro TYPE INTEGER;

-- Atualiza a sequência do registro_auditoria para INTEGER
ALTER SEQUENCE registro_auditoria_id_registro_seq AS INTEGER;

-- Remove o repositório tipo_usuario que está deprecated
DROP TABLE IF EXISTS tipo_usuario CASCADE;

-- Atualiza o tipo do ID do usuarios para UUID
ALTER TABLE usuarios 
    ALTER COLUMN id TYPE UUID USING id::uuid;

-- Atualiza as referências ao ID do usuário em outras tabelas
ALTER TABLE encomenda 
    ALTER COLUMN id_usuario TYPE UUID USING id_usuario::uuid;

ALTER TABLE notificacao 
    ALTER COLUMN id_usuario TYPE UUID USING id_usuario::uuid; 