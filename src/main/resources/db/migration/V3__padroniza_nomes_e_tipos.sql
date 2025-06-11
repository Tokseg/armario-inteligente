-- Padronização de nomes, tipos e constraints

-- Renomear tabelas para lowercase (caso existam com letra maiúscula)
ALTER TABLE IF EXISTS "Armario" RENAME TO armario;
ALTER TABLE IF EXISTS "Encomenda" RENAME TO encomenda;
ALTER TABLE IF EXISTS "Notificacao" RENAME TO notificacao;
ALTER TABLE IF EXISTS "Compartimento" RENAME TO compartimento;

-- Renomear colunas para snake_case
DO $$
BEGIN
    -- Verifica e renomeia colunas da tabela encomenda
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'encomenda' AND column_name = 'idArmario') THEN
        ALTER TABLE encomenda RENAME COLUMN "idArmario" TO id_armario;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'encomenda' AND column_name = 'idEncomenda') THEN
        ALTER TABLE encomenda RENAME COLUMN "idEncomenda" TO id_encomenda;
    END IF;
    
    -- Verifica e renomeia colunas da tabela compartimento
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'compartimento' AND column_name = 'idArmario') THEN
        ALTER TABLE compartimento RENAME COLUMN "idArmario" TO id_armario;
    END IF;
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'compartimento' AND column_name = 'idEncomendaAtual') THEN
        ALTER TABLE compartimento RENAME COLUMN "idEncomendaAtual" TO id_encomenda_atual;
    END IF;
END
$$;

-- Atualizar tipo do id_compartimento para UUID
ALTER TABLE compartimento ALTER COLUMN id_compartimento DROP DEFAULT;
ALTER TABLE compartimento ALTER COLUMN id_compartimento TYPE UUID USING gen_random_uuid();
ALTER TABLE compartimento ALTER COLUMN id_compartimento SET DEFAULT gen_random_uuid();

-- Atualizar constraints de foreign key
ALTER TABLE encomenda DROP CONSTRAINT IF EXISTS fk_encomenda_armario;
ALTER TABLE compartimento DROP CONSTRAINT IF EXISTS fk_compartimento_armario;
ALTER TABLE compartimento DROP CONSTRAINT IF EXISTS fk_compartimento_encomenda;
ALTER TABLE armario DROP CONSTRAINT IF EXISTS fk_armario_encomenda;

-- Atualizar tipo do id_registro para INTEGER
ALTER TABLE registro_auditoria ALTER COLUMN id_registro TYPE INTEGER;
ALTER SEQUENCE IF EXISTS registro_auditoria_id_registro_seq AS INTEGER;

-- Remover coluna tipo_usuario_id se ainda existir
ALTER TABLE usuarios DROP COLUMN IF EXISTS tipo_usuario_id; 