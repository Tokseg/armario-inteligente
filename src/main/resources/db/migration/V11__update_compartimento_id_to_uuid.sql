-- Atualizar o tipo da coluna id_compartimento para UUID
ALTER TABLE compartimento
    ALTER COLUMN id_compartimento TYPE UUID USING (gen_random_uuid());

-- Atualizar a sequência para usar UUID ao invés de BIGSERIAL
ALTER TABLE compartimento
    ALTER COLUMN id_compartimento SET DEFAULT gen_random_uuid();
