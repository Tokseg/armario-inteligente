-- Cria uma coluna temporária para armazenar os IDs convertidos
ALTER TABLE registro_auditoria ADD COLUMN id_new INTEGER;

-- Copia os valores existentes para a nova coluna
UPDATE registro_auditoria SET id_new = id_registro::INTEGER;

-- Remove a coluna antiga e a constraint
ALTER TABLE registro_auditoria DROP CONSTRAINT IF EXISTS registro_auditoria_pkey;
ALTER TABLE registro_auditoria DROP COLUMN id_registro;

-- Renomeia a nova coluna para o nome original
ALTER TABLE registro_auditoria RENAME COLUMN id_new TO id_registro;

-- Adiciona a constraint PRIMARY KEY novamente
ALTER TABLE registro_auditoria ADD PRIMARY KEY (id_registro);

-- Altera a sequência para INTEGER
ALTER SEQUENCE registro_auditoria_id_registro_seq AS INTEGER; 