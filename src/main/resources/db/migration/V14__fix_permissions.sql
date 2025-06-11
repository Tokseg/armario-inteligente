-- Concede todas as permissões necessárias ao usuário postgres
GRANT ALL PRIVILEGES ON DATABASE armario TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;
GRANT ALL PRIVILEGES ON SCHEMA public TO postgres;

-- Garante que o usuário postgres tenha acesso à tabela flyway_schema_history
GRANT ALL PRIVILEGES ON TABLE flyway_schema_history TO postgres; 