-- Corrige as inconsistências entre a entidade JPA e o banco de dados

-- 1. Adiciona a coluna id (UUID) se não existir
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'armario' AND column_name = 'id') THEN
        ALTER TABLE armario ADD COLUMN id UUID;
    END IF;
END
$$;

-- 2. Preenche a coluna id (UUID) para cada armário existente
UPDATE armario SET id = gen_random_uuid() WHERE id IS NULL;

-- 3. Adiciona a coluna armario_id (UUID) em encomenda e compartimento
ALTER TABLE encomenda ADD COLUMN IF NOT EXISTS armario_id UUID;
ALTER TABLE compartimento ADD COLUMN IF NOT EXISTS armario_id UUID;

-- 4. Atualiza as FKs para apontar para o novo id
UPDATE encomenda e
SET armario_id = a.id
FROM armario a
WHERE e.id_armario = a.id_armario;

UPDATE compartimento c
SET armario_id = a.id
FROM armario a
WHERE c.id_armario = a.id_armario;

-- 5. Remove as FKs antigas
ALTER TABLE encomenda DROP CONSTRAINT IF EXISTS fk_encomenda_armario;
ALTER TABLE compartimento DROP CONSTRAINT IF EXISTS fk_compartimento_armario;
ALTER TABLE armario DROP CONSTRAINT IF EXISTS fk_armario_encomenda;

-- 6. Remove as colunas antigas de FK
ALTER TABLE encomenda DROP COLUMN IF EXISTS id_armario;
ALTER TABLE compartimento DROP COLUMN IF EXISTS id_armario;

-- 7. Remove a PK antiga e define a nova PK
ALTER TABLE armario DROP CONSTRAINT IF EXISTS armario_pkey;
ALTER TABLE armario ALTER COLUMN id SET NOT NULL;
ALTER TABLE armario ADD PRIMARY KEY (id);

-- 8. Adiciona as novas FKs
ALTER TABLE encomenda
    ADD CONSTRAINT fk_encomenda_armario FOREIGN KEY (armario_id) REFERENCES armario(id);

ALTER TABLE compartimento
    ADD CONSTRAINT fk_compartimento_armario FOREIGN KEY (armario_id) REFERENCES armario(id);

-- 9. Atualiza a coluna id_encomenda_atual na tabela armario para usar a nova FK
ALTER TABLE armario
    ADD CONSTRAINT fk_armario_encomenda FOREIGN KEY (id_encomenda_atual) REFERENCES encomenda(id_encomenda);

-- 10. Remove a coluna id_armario da tabela armario
ALTER TABLE armario DROP COLUMN IF EXISTS id_armario; 