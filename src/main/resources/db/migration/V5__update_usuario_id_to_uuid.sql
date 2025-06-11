-- Cria colunas temporárias para armazenar os novos IDs UUID
ALTER TABLE usuarios ADD COLUMN id_new UUID;
ALTER TABLE encomenda ADD COLUMN id_usuario_new UUID;
ALTER TABLE notificacao ADD COLUMN id_usuario_new UUID;

-- Gera novos UUIDs para os usuários existentes
UPDATE usuarios SET id_new = gen_random_uuid();

-- Atualiza as referências nas outras tabelas
UPDATE encomenda e
SET id_usuario_new = u.id_new
FROM usuarios u
WHERE e.id_usuario = u.id_usuario;

UPDATE notificacao n
SET id_usuario_new = u.id_new
FROM usuarios u
WHERE n.id_usuario = u.id_usuario;

-- Remove as constraints existentes
ALTER TABLE encomenda DROP CONSTRAINT IF EXISTS fk_encomenda_usuario;
ALTER TABLE notificacao DROP CONSTRAINT IF EXISTS fk_notificacao_usuario;

-- Remove as colunas antigas
ALTER TABLE usuarios DROP COLUMN id_usuario;
ALTER TABLE encomenda DROP COLUMN id_usuario;
ALTER TABLE notificacao DROP COLUMN id_usuario;

-- Renomeia as novas colunas
ALTER TABLE usuarios RENAME COLUMN id_new TO id;
ALTER TABLE encomenda RENAME COLUMN id_usuario_new TO id_usuario;
ALTER TABLE notificacao RENAME COLUMN id_usuario_new TO id_usuario;

-- Adiciona as constraints novamente
ALTER TABLE usuarios ADD PRIMARY KEY (id);
ALTER TABLE encomenda ADD CONSTRAINT fk_encomenda_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id);
ALTER TABLE notificacao ADD CONSTRAINT fk_notificacao_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id);

-- Remove a coluna tipo_usuario_id que não é mais necessária (se ela existir)
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'usuarios' AND column_name = 'tipo_usuario_id') THEN
    ALTER TABLE usuarios DROP COLUMN tipo_usuario_id;
  END IF;
END
$$; 