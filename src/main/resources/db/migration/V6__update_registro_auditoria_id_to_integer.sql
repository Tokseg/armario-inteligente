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

-- Altera o tipo da coluna para INTEGER
ALTER TABLE registro_auditoria ALTER COLUMN id_registro TYPE INTEGER;

-- (Opcional) Reinicia a sequência, se necessário
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_class WHERE relkind = 'S' AND relname = 'registro_auditoria_id_registro_seq') THEN
    ALTER SEQUENCE registro_auditoria_id_registro_seq RESTART WITH 1;
  END IF;
END
$$; 