-- Remove a coluna ocupado da tabela armario, pois agora usamos o enum status
ALTER TABLE armario DROP COLUMN IF EXISTS ocupado; 