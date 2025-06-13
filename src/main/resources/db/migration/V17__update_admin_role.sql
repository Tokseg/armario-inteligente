-- Atualiza o tipo de usuário de ADMINISTRADOR para ADMIN
UPDATE usuarios SET tipo = 'ADMIN' WHERE tipo = 'ADMINISTRADOR';

-- Atualiza o comentário da coluna
COMMENT ON COLUMN usuarios.tipo IS 'Tipo do usuário (ADMIN, PORTEIRO, MORADOR)'; 