-- Adiciona as colunas se não existirem
ALTER TABLE armario ADD COLUMN IF NOT EXISTS localizacao VARCHAR(255);
ALTER TABLE armario ADD COLUMN IF NOT EXISTS numero VARCHAR(255);
ALTER TABLE armario ADD COLUMN IF NOT EXISTS status SMALLINT;

-- Atualiza os registros existentes com valores padrão
UPDATE armario
SET localizacao = 'Portaria',
    numero = 'A' || id_armario,
    status = 0
WHERE localizacao IS NULL;

-- Adiciona a constraint de unicidade para numero
ALTER TABLE armario
    ADD CONSTRAINT UK_armario_numero UNIQUE (numero);

-- Altera as colunas para NOT NULL (uma por vez para compatibilidade com H2)
ALTER TABLE armario ALTER COLUMN localizacao SET NOT NULL;
ALTER TABLE armario ALTER COLUMN numero SET NOT NULL;
ALTER TABLE armario ALTER COLUMN status SET NOT NULL;

-- Adiciona a constraint de check para status
ALTER TABLE armario
    ADD CONSTRAINT CK_armario_status CHECK (status BETWEEN 0 AND 2); 