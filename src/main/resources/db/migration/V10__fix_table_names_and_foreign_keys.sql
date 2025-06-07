-- Padronizar os nomes das tabelas para lowercase
ALTER TABLE IF EXISTS "Compartimento" RENAME TO compartimento;
ALTER TABLE IF EXISTS "Encomenda" RENAME TO encomenda;
ALTER TABLE IF EXISTS "Notificacao" RENAME TO notificacao;

-- Atualizar as referências para usar o schema correto
ALTER TABLE compartimento
    DROP CONSTRAINT IF EXISTS fk_compartimento_armario,
    DROP CONSTRAINT IF EXISTS fk_compartimento_encomenda;

ALTER TABLE encomenda
    DROP CONSTRAINT IF EXISTS fk_encomenda_armario,
    DROP CONSTRAINT IF EXISTS fk_encomenda_usuario;

ALTER TABLE notificacao
    DROP CONSTRAINT IF EXISTS fk_notificacao_usuario;

-- Recriar as constraints com os nomes das tabelas corretos
ALTER TABLE compartimento
    ADD CONSTRAINT fk_compartimento_armario FOREIGN KEY (id_armario) REFERENCES armario(id_armario),
    ADD CONSTRAINT fk_compartimento_encomenda FOREIGN KEY (id_encomenda_atual) REFERENCES encomenda(id_encomenda);

ALTER TABLE encomenda
    ADD CONSTRAINT fk_encomenda_armario FOREIGN KEY (id_armario) REFERENCES armario(id_armario),
    ADD CONSTRAINT fk_encomenda_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id);

ALTER TABLE notificacao
    ADD CONSTRAINT fk_notificacao_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios(id);
