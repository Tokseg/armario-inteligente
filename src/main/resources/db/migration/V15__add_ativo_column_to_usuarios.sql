-- Adiciona a coluna ativo à tabela usuarios
ALTER TABLE usuarios ADD COLUMN ativo BOOLEAN NOT NULL DEFAULT true;

-- Atualiza os registros existentes para ter ativo = true
UPDATE usuarios SET ativo = true WHERE ativo IS NULL;

-- Adiciona um comentário na coluna para documentação
COMMENT ON COLUMN usuarios.ativo IS 'Indica se o usuário está ativo no sistema'; 