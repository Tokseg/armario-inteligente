# Armário Inteligente

Sistema de gerenciamento de armários inteligentes para condomínios, desenvolvido com Spring Boot e PostgreSQL.

## 📋 Descrição

O Armário Inteligente é uma solução completa para gerenciamento de armários de encomendas em condomínios. O sistema permite o controle de armários, compartimentos, encomendas e usuários, além de oferecer notificações e registro de auditoria.

## 🚀 Funcionalidades

- **Gestão de Usuários**
  - Cadastro de moradores, porteiros e administradores
  - Autenticação JWT
  - Controle de permissões por tipo de usuário

- **Gestão de Armários**
  - Cadastro de armários com localização
  - Controle de status (DISPONÍVEL, OCUPADO, MANUTENÇÃO)
  - Gerenciamento de compartimentos

- **Gestão de Encomendas**
  - Registro de encomendas
  - Associação com armários e usuários
  - Controle de entrega

- **Notificações**
  - Sistema de notificações para usuários
  - Alertas de novas encomendas
  - Histórico de notificações

- **Auditoria**
  - Registro de todas as operações
  - Histórico de ações
  - Rastreabilidade

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway (Migração de banco de dados)
- JWT (Autenticação)
- Lombok
- Maven
- Docker

## 📦 Pré-requisitos

- JDK 17 ou superior
- Maven 3.8+
- PostgreSQL 12+
- Docker (opcional)
- IDE (recomendado: IntelliJ IDEA ou Eclipse)

## 🔧 Instalação

1. Clone o repositório:
```bash
git clone https://github.com/Tokseg/armario-inteligente.git
cd armario-inteligente
```

2. Configure o banco de dados:
   - Crie um banco de dados PostgreSQL chamado `armario`
   - Configure as credenciais no arquivo `application.properties`

3. Execute o projeto:
```bash
# Usando Maven
mvn spring-boot:run

# Ou usando Docker
docker-compose up -d --build
```

## ⚙️ Configuração

### Banco de Dados
Edite o arquivo `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/armario
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### Docker
O arquivo `docker-compose.yml` já está configurado com:
- PostgreSQL
- pgAdmin (opcional)
- Aplicação Spring Boot

## 📚 Documentação da API

### Autenticação

#### Registrar Usuário
```http
POST /api/v1/auth/register
Content-Type: application/json

{
    "nome": "Nome do Usuário",
    "email": "usuario@email.com",
    "senha": "senha123",
    "telefone": "11999999999",
    "tipo": "MORADOR"
}
```

#### Login
```http
POST /api/v1/auth/authenticate
Content-Type: application/json

{
    "email": "usuario@email.com",
    "senha": "senha123"
}
```

### Usuários

#### Listar Usuários
```http
GET /api/usuarios
Authorization: Bearer {token}
```

#### Buscar Usuário
```http
GET /api/usuarios/{id}
Authorization: Bearer {token}
```

#### Criar Usuário
```http
POST /api/usuarios
Authorization: Bearer {token}
Content-Type: application/json

{
    "nome": "Novo Usuário",
    "email": "novo@email.com",
    "senha": "senha123",
    "telefone": "11988888888",
    "tipo": "MORADOR"
}
```

### Armários

#### Listar Armários
```http
GET /api/armarios
Authorization: Bearer {token}
```

#### Buscar Armário
```http
GET /api/armarios/{id}
Authorization: Bearer {token}
```

#### Criar Armário (ADMIN)
```http
POST /api/armarios
Authorization: Bearer {token}
Content-Type: application/json

{
    "numero": "A1",
    "localizacao": "Portaria",
    "status": "DISPONIVEL"
}
```

#### Atualizar Status (ADMIN)
```http
PUT /api/armarios/{id}/status?novoStatus=OCUPADO
Authorization: Bearer {token}
```

### Encomendas

#### Listar Encomendas
```http
GET /api/encomendas
Authorization: Bearer {token}
```

#### Criar Encomenda
```http
POST /api/encomendas
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

### Notificações

#### Listar Notificações
```http
GET /api/notificacoes
Authorization: Bearer {token}
```

#### Criar Notificação (ADMIN)
```http
POST /api/notificacoes
Authorization: Bearer {token}
Content-Type: application/json

{
    "mensagem": "Sua encomenda chegou!",
    "usuario": {
        "id": "uuid-do-usuario"
    }
}
```

### Auditoria

#### Listar Registros (ADMIN)
```http
GET /api/auditoria
Authorization: Bearer {token}
```

## 🔐 Segurança

- Autenticação JWT
- Senhas criptografadas com BCrypt
- Controle de acesso baseado em roles (ADMIN, PORTEIRO, MORADOR)
- Validação de dados
- Proteção contra CSRF
- Headers de segurança

## 🧪 Testes

Execute os testes com:
```bash
mvn test
```

## 📦 Estrutura do Projeto

```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/unit/tokseg/armario_inteligente/
│   │   │       ├── config/          # Configurações
│   │   │       ├── controller/      # Controllers REST
│   │   │       ├── dto/            # Objetos de transferência
│   │   │       ├── model/          # Entidades
│   │   │       ├── repository/     # Repositórios JPA
│   │   │       ├── service/        # Serviços
│   │   │       └── util/           # Utilitários
│   │   └── resources/
│   │       ├── db/migration/       # Scripts Flyway
│   │       └── application.properties
│   └── test/                       # Testes
├── docker/                         # Configurações Docker
│   ├── Dockerfile                 # Dockerfile da aplicação
│   └── docker-compose.yml         # Configuração dos containers
├── .gitignore                     # Arquivos ignorados pelo Git
├── .gitattributes                 # Configurações do Git
├── pom.xml                        # Configuração Maven
├── mvnw                           # Wrapper Maven (Unix)
├── mvnw.cmd                       # Wrapper Maven (Windows)
└── README.md                      # Este arquivo
```
## ✨ Autor
- Hugo Machado Ramos- (https://github.com/Hugo-M-R)

