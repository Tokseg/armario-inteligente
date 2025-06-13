-- Concede todas as permissões necessárias ao usuário armario
GRANT ALL PRIVILEGES ON DATABASE armario TO armario;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO armario;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO armario;
GRANT ALL PRIVILEGES ON SCHEMA public TO armario;

-- Garante que o usuário armario tenha acesso à tabela flyway_schema_history
GRANT ALL PRIVILEGES ON TABLE flyway_schema_history TO armario; 