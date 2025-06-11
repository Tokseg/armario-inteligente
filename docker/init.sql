-- Cria o usuário armario se não existir
DO
$do$
BEGIN
   IF NOT EXISTS (
      SELECT FROM pg_catalog.pg_roles
      WHERE  rolname = 'armario') THEN

      CREATE USER armario WITH PASSWORD 'armario' CREATEDB;
   END IF;
END
$do$;

-- Concede todas as permissões no banco de dados para o usuário armario
GRANT ALL PRIVILEGES ON DATABASE armario TO armario;

-- Conecta ao banco armario
\c armario

-- Garante que o usuário armario seja o dono do schema public
ALTER SCHEMA public OWNER TO armario;

-- Concede todas as permissões no schema public para o usuário armario
GRANT ALL ON SCHEMA public TO armario;
GRANT ALL ON ALL TABLES IN SCHEMA public TO armario;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO armario;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public TO armario;

-- Garante que novas tabelas criadas pelo usuário armario tenham as permissões corretas
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO armario;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO armario;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON FUNCTIONS TO armario;

-- Garante que o usuário armario tenha permissão para criar tabelas
GRANT CREATE ON SCHEMA public TO armario; 