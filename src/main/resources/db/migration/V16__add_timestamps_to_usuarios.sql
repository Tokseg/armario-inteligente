-- Adiciona as colunas de timestamp à tabela usuarios
ALTER TABLE usuarios ADD COLUMN data_criacao TIMESTAMP;
ALTER TABLE usuarios ADD COLUMN data_atualizacao TIMESTAMP;

-- Adiciona a coluna tipo para o enum TipoUsuarioEnum
ALTER TABLE usuarios ADD COLUMN tipo VARCHAR(255);

-- Adiciona colunas de auditoria à tabela usuarios
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP;

-- Atualiza os registros existentes
UPDATE usuarios SET 
    data_criacao = CURRENT_TIMESTAMP,
    data_atualizacao = CURRENT_TIMESTAMP,
    tipo = 'MORADOR' -- valor padrão para registros existentes
WHERE data_criacao IS NULL OR data_atualizacao IS NULL OR tipo IS NULL;

-- Torna as colunas NOT NULL após atualizar os registros existentes
ALTER TABLE usuarios ALTER COLUMN data_criacao SET NOT NULL;
ALTER TABLE usuarios ALTER COLUMN data_atualizacao SET NOT NULL;
ALTER TABLE usuarios ALTER COLUMN tipo SET NOT NULL;

-- Adiciona comentários nas colunas para documentação
COMMENT ON COLUMN usuarios.data_criacao IS 'Data e hora de criação do registro do usuário';
COMMENT ON COLUMN usuarios.data_atualizacao IS 'Data e hora da última atualização do registro do usuário';
COMMENT ON COLUMN usuarios.tipo IS 'Tipo do usuário: ADMIN, FUNCIONARIO, MORADOR';

-- Atualiza o tipo do usuário admin para ADMIN (se existir)
UPDATE usuarios SET tipo = 'ADMIN' WHERE tipo = 'admin'; 