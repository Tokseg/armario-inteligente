-- Atualiza a estrutura da tabela registro_auditoria

-- 1. Aumenta o tamanho dos campos VARCHAR para TEXT
ALTER TABLE registro_auditoria 
    ALTER COLUMN acao TYPE TEXT,
    ALTER COLUMN detalhes TYPE TEXT;

-- 2. Cria uma coluna temporária para armazenar os IDs convertidos
ALTER TABLE registro_auditoria ADD COLUMN id_new INTEGER;

-- 3. Copia os valores existentes para a nova coluna
UPDATE registro_auditoria SET id_new = id_registro::INTEGER;

-- 4. Remove a coluna antiga e a constraint
ALTER TABLE registro_auditoria DROP CONSTRAINT IF EXISTS registro_auditoria_pkey;
ALTER TABLE registro_auditoria DROP COLUMN id_registro;

-- 5. Renomeia a nova coluna para o nome original
ALTER TABLE registro_auditoria RENAME COLUMN id_new TO id_registro;

-- 6. Adiciona a constraint PRIMARY KEY novamente
ALTER TABLE registro_auditoria ADD PRIMARY KEY (id_registro);

-- 7. Configura a sequência
DO $$
BEGIN
    -- Verifica se a sequência existe
    IF EXISTS (SELECT 1 FROM pg_class WHERE relkind = 'S' AND relname = 'registro_auditoria_id_registro_seq') THEN
        -- Reinicia a sequência
        ALTER SEQUENCE registro_auditoria_id_registro_seq RESTART WITH 1;
    ELSE
        -- Cria a sequência se não existir
        CREATE SEQUENCE registro_auditoria_id_registro_seq;
    END IF;
END
$$;

-- 8. Configura o valor padrão da coluna id_registro
ALTER TABLE registro_auditoria 
    ALTER COLUMN id_registro SET DEFAULT nextval('registro_auditoria_id_registro_seq');

-- 9. Adiciona comentários para documentação
COMMENT ON TABLE registro_auditoria IS 'Registra as ações realizadas no sistema para fins de auditoria';
COMMENT ON COLUMN registro_auditoria.id_registro IS 'Identificador único do registro de auditoria';
COMMENT ON COLUMN registro_auditoria.acao IS 'Tipo da ação realizada (ex: LOGIN_USUARIO, CADASTRO_ENCOMENDA)';
COMMENT ON COLUMN registro_auditoria.detalhes IS 'Detalhes da ação realizada';
COMMENT ON COLUMN registro_auditoria.data_hora IS 'Data e hora em que a ação foi realizada'; 