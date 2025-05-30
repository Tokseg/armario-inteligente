-- Verifica se o banco existe
SELECT datname FROM pg_database WHERE datname = 'armario';

-- Cria o banco se não existir
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'armario') THEN
        CREATE DATABASE armario;
    END IF;
END
$$;

-- Conecta ao banco armario
\c armario

-- Verifica se o usuário existe
SELECT usename FROM pg_user WHERE usename = 'armario';

-- Cria o usuário se não existir
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_user WHERE usename = 'armario') THEN
        CREATE USER armario WITH PASSWORD 'armario';
    END IF;
END
$$;

-- Concede todas as permissões ao usuário
GRANT ALL PRIVILEGES ON DATABASE armario TO armario;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO armario;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO armario;
GRANT ALL PRIVILEGES ON SCHEMA public TO armario; 