DO $$
BEGIN
    -- Verifica se a coluna status existe
    IF EXISTS (SELECT 1 FROM information_schema.columns 
              WHERE table_name = 'armario' AND column_name = 'status') THEN
        -- Cria uma coluna temporária para armazenar os valores convertidos
        ALTER TABLE armario ADD COLUMN status_new VARCHAR(255);

        -- Converte os valores numéricos para strings
        UPDATE armario
        SET status_new = CASE status
            WHEN 0 THEN 'DISPONIVEL'
            WHEN 1 THEN 'OCUPADO'
            WHEN 2 THEN 'MANUTENCAO'
            ELSE 'DISPONIVEL'
        END;

        -- Remove a coluna antiga e a constraint
        ALTER TABLE armario DROP CONSTRAINT IF EXISTS CK_armario_status;
        ALTER TABLE armario DROP COLUMN status;

        -- Renomeia a nova coluna para o nome original
        ALTER TABLE armario RENAME COLUMN status_new TO status;

        -- Adiciona a constraint NOT NULL
        ALTER TABLE armario ALTER COLUMN status SET NOT NULL;
    ELSE
        -- Se a coluna não existe, apenas cria ela com o valor padrão
        ALTER TABLE armario ADD COLUMN status VARCHAR(255) NOT NULL DEFAULT 'DISPONIVEL';
    END IF;
END
$$; 