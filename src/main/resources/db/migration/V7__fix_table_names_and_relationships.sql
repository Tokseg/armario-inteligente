-- Corrige os nomes das tabelas para minúsculo
ALTER TABLE IF EXISTS "Armario" RENAME TO armario;
ALTER TABLE IF EXISTS "Encomenda" RENAME TO encomenda;
ALTER TABLE IF EXISTS "Notificacao" RENAME TO notificacao;
ALTER TABLE IF EXISTS "Compartimento" RENAME TO compartimento;

-- Remove a tabela tipo_usuario que está deprecated
DROP TABLE IF EXISTS tipo_usuario CASCADE;

-- Atualiza as foreign keys para usar UUID
ALTER TABLE encomenda 
    DROP CONSTRAINT IF EXISTS fk_encomenda_usuario,
    ALTER COLUMN id_usuario TYPE UUID USING id_usuario::uuid;

ALTER TABLE notificacao 
    DROP CONSTRAINT IF EXISTS fk_notificacao_usuario,
    ALTER COLUMN id_usuario TYPE UUID USING id_usuario::uuid;

-- Adiciona as foreign keys atualizadas
ALTER TABLE encomenda 
    ADD CONSTRAINT fk_encomenda_usuario 
    FOREIGN KEY (id_usuario) 
    REFERENCES usuarios(id);

ALTER TABLE notificacao 
    ADD CONSTRAINT fk_notificacao_usuario 
    FOREIGN KEY (id_usuario) 
    REFERENCES usuarios(id);

-- Remove a coluna tipo_usuario_id da tabela usuarios
ALTER TABLE usuarios DROP COLUMN IF EXISTS tipo_usuario_id; 