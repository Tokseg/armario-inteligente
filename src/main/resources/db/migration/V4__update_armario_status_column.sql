DO $$
BEGIN
    -- Remove a coluna ocupado da tabela armario, pois agora usamos o enum status
    ALTER TABLE armario DROP COLUMN IF EXISTS ocupado;

    -- Verifica se a coluna status existe
    IF EXISTS (SELECT 1 FROM information_schema.columns 
              WHERE table_name = 'armario' AND column_name = 'status') THEN
        
        -- Remove constraints existentes
        ALTER TABLE armario DROP CONSTRAINT IF EXISTS CK_armario_status;
        ALTER TABLE armario DROP CONSTRAINT IF EXISTS CK_armario_status_valid;

        -- Cria uma coluna temporária para armazenar os valores convertidos
        ALTER TABLE armario ADD COLUMN status_new VARCHAR(255);

        -- Converte os valores numéricos para strings
        UPDATE armario
        SET status_new = CASE 
            WHEN status ~ '^[0-9]+$' THEN
                CASE CAST(status AS INTEGER)
                    WHEN 0 THEN 'DISPONIVEL'
                    WHEN 1 THEN 'OCUPADO'
                    WHEN 2 THEN 'MANUTENCAO'
                    ELSE 'DISPONIVEL'
                END
            ELSE status
        END;

        -- Remove a coluna antiga
        ALTER TABLE armario DROP COLUMN status;

        -- Renomeia a nova coluna para o nome original
        ALTER TABLE armario RENAME COLUMN status_new TO status;

        -- Adiciona as constraints
        ALTER TABLE armario ALTER COLUMN status SET NOT NULL;
        ALTER TABLE armario ADD CONSTRAINT CK_armario_status_valid 
            CHECK (status IN ('DISPONIVEL', 'OCUPADO', 'MANUTENCAO'));
    ELSE
        -- Se a coluna não existe, cria ela com o valor padrão
        ALTER TABLE armario ADD COLUMN status VARCHAR(255) NOT NULL DEFAULT 'DISPONIVEL';
        ALTER TABLE armario ADD CONSTRAINT CK_armario_status_valid 
            CHECK (status IN ('DISPONIVEL', 'OCUPADO', 'MANUTENCAO'));
    END IF;
END
$$; 