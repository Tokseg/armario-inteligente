-- Atualiza o tipo do ID do compartimento para UUID
ALTER TABLE compartimento 
    ALTER COLUMN id_compartimento TYPE UUID USING gen_random_uuid();

-- Atualiza as referências ao ID do compartimento em outras tabelas
ALTER TABLE encomenda 
    ALTER COLUMN id_compartimento TYPE UUID USING (CASE WHEN id_compartimento IS NOT NULL THEN gen_random_uuid() ELSE NULL END);

ALTER TABLE registro_auditoria 
    ALTER COLUMN id_compartimento TYPE UUID USING (CASE WHEN id_compartimento IS NOT NULL THEN gen_random_uuid() ELSE NULL END);
