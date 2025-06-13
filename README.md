# Armário Inteligente

Sistema de gerenciamento de armários inteligentes para condomínios, desenvolvido com Spring Boot e PostgreSQL.

## 📋 Descrição

O Armário Inteligente é uma solução completa para gerenciamento de armários de encomendas em condomínios. O sistema permite o controle de armários, compartimentos, encomendas e usuários, além de oferecer notificações e registro de auditoria.

## 🚀 Funcionalidades

- **Gestão de Usuários**
  - Cadastro de moradores, porteiros e administradores
  - Autenticação JWT
  - Controle de permissões por tipo de usuário
  - Ativação/desativação de usuários
  - Registro de data de criação e atualização

- **Gestão de Armários**
  - Cadastro de armários com localização
  - Controle de status (DISPONÍVEL, OCUPADO, MANUTENÇÃO)
  - Gerenciamento de compartimentos
  - Histórico de alterações

- **Gestão de Encomendas**
  - Registro de encomendas
  - Associação com armários e usuários
  - Controle de entrega
  - Notificações automáticas

- **Notificações**
  - Sistema de notificações para usuários
  - Alertas de novas encomendas
  - Histórico de notificações
  - Status de leitura

- **Auditoria**
  - Registro automático de todas as operações
  - Histórico detalhado de ações
  - Rastreabilidade completa
  - Logs de sistema

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.3
- Spring Security com JWT
- Spring Data JPA
- PostgreSQL 15
- Flyway (Migração de banco de dados)
- Lombok
- Maven
- Docker e Docker Compose
- Spring AOP (Aspectos para auditoria)

## 🚀 Roadmap para Ambiente de Produção

Este projeto foi desenvolvido como um trabalho acadêmico, mas para um ambiente de produção real, as seguintes funcionalidades seriam necessárias:

### 1. Sistema de Retirada de Encomendas
- **Endpoints de Retirada**:
  - `POST /api/encomendas/{id}/retirar`: Endpoint para solicitar retirada de encomenda
  - `POST /api/encomendas/{id}/gerar-codigo`: Geração de código de acesso temporário
  - `POST /api/encomendas/{id}/validar-codigo`: Validação do código de acesso

- **Campos Adicionais na Entidade Encomenda**:
  ```java
  private LocalDateTime dataRetirada;
  private StatusRetirada statusRetirada;
  private String codigoAcesso;
  private LocalDateTime dataExpiracaoCodigo;
  ```

### 2. Integração com Hardware
- **Sistema de Trancas Eletrônicas**:
  - Integração com APIs de trancas inteligentes
  - Sistema de liberação remota de compartimentos
  - Monitoramento de status das trancas
  - Sistema de backup para falhas de energia

- **Serviços de Hardware**:
  ```java
  public class HardwareService {
      public void liberarCompartimento(UUID compartimentoId, String codigo);
      public void verificarStatusTranca(UUID compartimentoId);
      public void registrarEventoTranca(UUID compartimentoId, EventoTranca evento);
  }
  ```

### 3. Segurança e Autenticação
- **Autenticação Multi-fator**:
  - Implementação de 2FA para acesso ao sistema
  - Validação por SMS/Email para retiradas
  - Biometria para acesso aos armários

- **Controle de Acesso Granular**:
  - Papéis adicionais (porteiro, zelador, entregador)
  - Permissões específicas por tipo de operação
  - Registro detalhado de tentativas de acesso

### 4. Notificações e Comunicação
- **Sistema de Notificações Avançado**:
  - Notificações push para dispositivos móveis
  - Integração com WhatsApp/Telegram
  - Templates personalizados por tipo de notificação
  - Sistema de confirmação de leitura

- **Comunicação com Moradores**:
  ```java
  public class NotificacaoService {
      public void enviarNotificacaoRetirada(Encomenda encomenda);
      public void enviarLembreteEncomenda(Encomenda encomenda);
      public void enviarAlertaTempoExcedido(Encomenda encomenda);
  }
  ```

### 5. Monitoramento e Manutenção
- **Sistema de Monitoramento**:
  - Dashboard em tempo real
  - Alertas de sistema
  - Métricas de uso
  - Relatórios de ocupação

- **Manutenção Preventiva**:
  - Agendamento de manutenção
  - Registro de problemas
  - Histórico de manutenções
  - Previsão de falhas

### 6. Integração com Sistemas Externos
- **APIs de Entregadores**:
  - Integração com sistemas de entregas
  - Rastreamento de encomendas
  - Confirmação de entrega
  - Notificações automáticas

- **Sistemas de Condomínio**:
  - Integração com portaria
  - Controle de acesso
  - Registro de visitantes
  - Comunicação com síndico

### 7. Backup e Recuperação
- **Sistema de Backup**:
  - Backup automático do banco de dados
  - Backup de configurações
  - Sistema de recuperação de desastres
  - Versionamento de dados

### 8. Documentação e Suporte
- **Documentação Técnica**:
  - Manual de instalação
  - Guia de configuração
  - Documentação de APIs
  - Guia de troubleshooting

- **Suporte ao Usuário**:
  - Sistema de tickets
  - FAQ automatizado
  - Chat de suporte
  - Base de conhecimento

### 9. Escalabilidade
- **Arquitetura Distribuída**:
  - Load balancing
  - Cache distribuído
  - Filas de mensagens
  - Microserviços

- **Otimizações**:
  - Indexação de banco de dados
  - Cache de consultas
  - Compressão de dados
  - CDN para arquivos estáticos

### 10. Conformidade e Auditoria
- **LGPD e Privacidade**:
  - Política de retenção de dados
  - Exportação de dados pessoais
  - Registro de consentimento
  - Anonimização de dados

- **Auditoria Avançada**:
  - Logs detalhados de operações
  - Rastreamento de mudanças
  - Relatórios de conformidade
  - Alertas de segurança

Estas funcionalidades representam um roadmap para transformar o projeto acadêmico em uma solução robusta e pronta para produção. A implementação deve ser priorizada com base nas necessidades específicas do ambiente de uso.

## 📦 Pré-requisitos

- JDK 17 ou superior
- Maven 3.8+
- Docker e Docker Compose
- IDE (recomendado: IntelliJ IDEA ou Eclipse)
- Postman (para testes da API)

## 🔧 Instalação

1. Clone o repositório:
```bash
git clone https://github.com/Tokseg/armario-inteligente.git
cd armario-inteligente
```

2. Execute com Docker:
```bash
docker-compose up -d --build
```

O sistema estará disponível em:
- API: http://localhost:8080
- PostgreSQL: localhost:5432
- PgAdmin: http://localhost:5050 (opcional)

## 📦 Estrutura do Projeto

```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/unit/tokseg/armario_inteligente/
│   │   │       ├── aspect/          # Aspectos (Auditoria)
│   │   │       ├── config/          # Configurações
│   │   │       ├── controller/      # Controllers REST
│   │   │       ├── dto/            # Objetos de transferência
│   │   │       ├── exception/      # Tratamento de exceções
│   │   │       ├── model/          # Entidades
│   │   │       ├── repository/     # Repositórios JPA
│   │   │       ├── security/       # Configurações de segurança
│   │   │       ├── service/        # Serviços
│   │   │       └── util/           # Utilitários
│   │   └── resources/
│   │       ├── db/migration/       # Scripts Flyway
│   │       ├── application.properties
│   │       └── application-docker.properties
│   └── test/                       # Testes
├── docker/                         # Configurações Docker
│   ├── Dockerfile                 # Dockerfile da aplicação
│   ├── docker-compose.yml         # Configuração dos containers
│   └── init.sql                   # Script de inicialização do banco
├── .gitignore                     # Arquivos ignorados pelo Git
├── .gitattributes                 # Configurações do Git
├── pom.xml                        # Configuração Maven
└── README.md                      # Este arquivo
```

## 📚 Testes da API

### Autenticação

#### Registrar Administrador
```http
POST http://localhost:8080/api/v1/auth/register
Content-Type: application/json

{
    "nome": "Admin Teste",
    "email": "admin@teste.com",
    "senha": "Admin@123",
    "telefone": "11999999999",
    "tipo": "ADMIN"
}
```

#### Login
```http
POST http://localhost:8080/api/v1/auth/authenticate
Content-Type: application/json

{
    "email": "admin@teste.com",
    "senha": "Admin@123"
}
```

### Usuários

#### Criar Morador
```http
POST http://localhost:8080/api/usuarios
Authorization: Bearer {token}
Content-Type: application/json

{
    "nome": "Morador Teste",
    "email": "morador@teste.com",
    "senha": "Morador@123",
    "telefone": "11988888888",
    "tipo": "MORADOR"
}
```

#### Listar Usuários
```http
GET http://localhost:8080/api/usuarios
Authorization: Bearer {token}
```

### Armários

#### Criar Armário
```http
POST http://localhost:8080/api/armarios
Authorization: Bearer {token}
Content-Type: application/json

{
    "numero": "A1",
    "localizacao": "Portaria",
    "status": "DISPONIVEL"
}
```

#### Listar Armários
```http
GET http://localhost:8080/api/armarios
Authorization: Bearer {token}
```

### Encomendas

#### Registrar Encomenda
```http
POST http://localhost:8080/api/encomendas
Authorization: Bearer {token}
Content-Type: application/json

{
    "descricao": "Caixa Amazon",
    "remetente": "Amazon",
    "armario": {
        "id": "uuid-do-armario"
    },
    "usuario": {
        "id": "uuid-do-usuario"
    }
}
```

#### Listar Encomendas
```http
GET http://localhost:8080/api/encomendas
Authorization: Bearer {token}
```

### Notificações

#### Listar Notificações
```http
GET http://localhost:8080/api/notificacoes
Authorization: Bearer {token}
```

### Auditoria

#### Listar Registros de Auditoria
```http
GET http://localhost:8080/api/auditoria
Authorization: Bearer {token}
```

## 🔐 Segurança

- Autenticação JWT com expiração configurável
- Senhas criptografadas com BCrypt
- Controle de acesso baseado em roles (ADMIN, PORTEIRO, MORADOR)
- Validação de dados com Bean Validation
- Proteção contra CSRF
- Headers de segurança
- Registro de auditoria automático
- Logs de segurança

## 🧪 Testes

Execute os testes com:
```bash
# Testes unitários
mvn test

# Testes de integração
mvn verify
```

## 📝 Notas de Atualização

### Versão 1.0.0
- Implementação do sistema de auditoria automática
- Adição de notificações automáticas
- Melhorias na segurança com JWT
- Suporte a Docker com PostgreSQL
- Migração de banco de dados com Flyway
- Documentação atualizada com exemplos Postman

## ✨ Autor
- Hugo Machado Ramos - [GitHub](https://github.com/Hugo-M-R)
