# Armário Inteligente

## Descrição Geral

O Armário Inteligente é um sistema para gerenciamento de armários destinados ao recebimento de encomendas em condomínios. Permite o registro, notificação e auditoria de entregas, integrando diferentes perfis de usuários (morador, porteiro, administrador) e mantendo o controle dos compartimentos do armário.

## Requisitos Técnicos

- Java 17 ou superior
- Maven 3.6 ou superior (ou Docker + docker-compose)
- Spring Boot 3.x
- PostgreSQL 12 (ou superior) (ou use o container Docker)
- IDE compatível com Java (recomendado: IntelliJ IDEA ou Eclipse)

## Instalação e Configuração

### Opção 1: Execução Local (sem Docker)

1. Clone o repositório:
   ```bash
   git clone https://github.com/Tokseg/armario-inteligente.git
   cd armario-inteligente
   ```

2. Configure o banco de dados (PostgreSQL):
   - Crie um banco de dados (ex: armario_inteligente) e um usuário (ex: armario) com senha (ex: armario).
   - (Opcional) Edite o arquivo `src/main/resources/application.properties` para sobrescrever as credenciais:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/armario_inteligente
     spring.datasource.username=armario
     spring.datasource.password=armario
     ```

3. Compile o projeto:
   ```bash
   mvn clean install
   ```

4. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```
   A aplicação estará disponível em:  
   http://localhost:8080

### Opção 2: Execução via Docker (Recomendado)

1. Clone o repositório:
   ```bash
   git clone https://github.com/Tokseg/armario-inteligente.git
   cd armario-inteligente
   ```

2. Na pasta `docker`, execute:
   ```bash
   docker-compose up -d
   ```
   Isso sobe os containers do PostgreSQL (db), da aplicação (app) e do pgAdmin (acessível em http://localhost:5050).

3. Acesse a aplicação em:  
   http://localhost:8081  
   (Obs.: a porta 8081 é mapeada para 8080 do container.)

---

## Estrutura do Projeto

```
docker/
 ├── Dockerfile (imagem da aplicação)
 ├── docker-compose.yml (orquestração dos containers)
 └── .dockerignore (arquivos ignorados na build)
src/
 └── main/
      ├── java/
      │    └── br/com/unit/tokseg/armario_inteligente/
      │         ├── controller/ (APIs REST)
      │         ├── model/ (entidades JPA)
      │         ├── repository/ (repositórios JPA)
      │         ├── service/ (lógica de negócio)
      │         ├── dto (objetos de transferência)
      │         └── config (configurações, segurança, etc.)
      └── resources/
           ├── application.properties (configuração central)
           ├── application-docker.properties (configuração Docker)
           └── db/migration (scripts Flyway)
```

---

## Camadas do Sistema

### 1. Model (Entidades)

#### Usuario
- **Atributos:**
  - `idUsuario` (UUID): identificador único.
  - `nome` (String): nome do usuário.
  - `email` (String): e-mail único.
  - `senha` (String): senha (não exposta em respostas).
  - `telefone` (String): telefone de contato.
  - `tipoUsuario` (TipoUsuarioEnum): tipo (MORADOR, PORTEIRO, ADMIN).
- **Relacionamentos:** Muitos usuários para um tipo de usuário.

#### TipoUsuarioEnum
- Enum (MORADOR, PORTEIRO, ADMIN) que define o perfil do usuário.

#### Compartimento
- **Atributos:**
  - `idCompartimento` (Long): identificador.
  - `armario` (Armario): armário ao qual pertence.
  - `ocupado` (boolean): indica se está ocupado.
  - `encomendaAtual` (Encomenda): encomenda armazenada (se houver).

#### Encomenda
- **Atributos:**
  - `idEncomenda` (String): identificador.
  - `descricao` (String): descrição da encomenda.
  - `remetente` (String): quem enviou.
  - `dataRecebimento` (LocalDateTime): data/hora do recebimento.
  - `idArmario` (Armario): armário onde está.
  - `idUsuario` (Usuario): destinatário.

#### Notificacao
- **Atributos:**
  - `idNotificacao` (String): identificador.
  - `usuario` (Usuario): destinatário.
  - `mensagem` (String): texto da notificação.
  - `dataEnvio` (LocalDateTime): data/hora do envio.

#### RegistroAuditoria
- **Atributos:**
  - `idRegistro` (int): identificador.
  - `acao` (String): ação realizada.
  - `detalhes` (String): detalhes da ação.
  - `dataHora` (LocalDateTime): data/hora do registro.

#### Armario
- **Atributos:**
  - `idArmario` (Long): identificador.
  - `ocupado` (boolean): se está ocupado.
  - `encomendaAtual` (Encomenda): encomenda armazenada (se houver).

---

### 2. Repository

Todos os repositórios estendem `JpaRepository`, fornecendo métodos CRUD prontos.

- **UsuarioRepository:** CRUD para Usuario.
- **CompartimentoRepository:** CRUD para Compartimento.
- **EncomendaRepository:** CRUD para Encomenda.
- **NotificacaoRepository:** CRUD para Notificacao.
- **RegistroAuditoriaRepository:** CRUD para RegistroAuditoria.
- **ArmarioRepository:** CRUD para Armario.

---

### 3. Service

Cada serviço encapsula a lógica de negócio e delega operações ao respectivo repositório.

Exemplo de métodos:
- `findAll()`: lista todos os registros.
- `findById(id)`: busca por ID.
- `save(obj)`: salva ou atualiza.
- `deleteById(id)`: remove por ID.

---

### 4. Controller (APIs REST)

#### AuthenticationController (Autenticação e Registro)
- **POST /api/v1/auth/register**  
  Registra um novo usuário.  
  **Body (RegisterRequest):**
  ```json
  {
    "nome": "Nome do Usuário",
    "email": "usuario@exemplo.com",
    "senha": "senha123",
    "telefone": "123456789",
    "tipo": "MORADOR"
  }
  ```
  **Resposta (AuthenticationResponse):**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```
- **POST /api/v1/auth/authenticate**  
  Autentica um usuário existente.  
  **Body (AuthenticationRequest):**
  ```json
  {
    "email": "usuario@exemplo.com",
    "senha": "senha123"
  }
  ```
  **Resposta (AuthenticationResponse):**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  ```

#### NotificacaoController
- **GET /api/notificacoes**  
  Lista todas as notificações (requer autenticação).
- **GET /api/notificacoes/{id}**  
  Busca notificação por ID (requer autenticação).
- **POST /api/notificacoes**  
  Cria uma nova notificação (requer autenticação).  
  **Body:**
  ```json
  {
    "idNotificacao": "string",
    "usuario": { ... },
    "mensagem": "string",
    "dataEnvio": "2023-01-01T12:00:00"
  }
  ```
- **DELETE /api/notificacoes/{id}**  
  Remove notificação por ID (requer autenticação).

#### RegistroAuditoriaController
- **GET /api/auditoria**  
  Lista todos os registros de auditoria (requer autenticação e permissão ADMIN).
- **GET /api/auditoria/{id}**  
  Busca registro por ID (requer autenticação e permissão ADMIN).
- **POST /api/auditoria**  
  Cria novo registro de auditoria (requer autenticação e permissão ADMIN).
- **DELETE /api/auditoria/{id}**  
  Remove registro por ID (requer autenticação e permissão ADMIN).

#### CompartimentoController
- **GET /api/compartimentos**  
  Lista todos os compartimentos (requer autenticação).
- **GET /api/compartimentos/{id}**  
  Busca compartimento por ID (requer autenticação).
- **POST /api/compartimentos**  
  Cria novo compartimento (requer autenticação e permissão ADMIN).
- **DELETE /api/compartimentos/{id}**  
  Remove compartimento por ID (requer autenticação e permissão ADMIN).

#### EncomendaController
- **GET /api/encomendas**  
  Lista todas as encomendas (requer autenticação).
- **GET /api/encomendas/{id}**  
  Busca encomenda por ID (requer autenticação).
- **POST /api/encomendas**  
  Cria nova encomenda (requer autenticação e permissão (PORTEIRO ou ADMIN)).  
  **Body:**
  ```json
  {
    "idEncomenda": "123",
    "descricao": "Caixa Amazon",
    "remetente": "Amazon",
    "dataRecebimento": "2023-01-01T12:00:00",
    "idArmario": { ... },
    "idUsuario": { ... }
  }
  ```
- **DELETE /api/encomendas/{id}**  
  Remove encomenda por ID (requer autenticação e permissão (PORTEIRO ou ADMIN)).

---

## Fluxos Principais

### Cadastro de Encomenda (Fluxo Completo)
1. O porteiro (ou administrador) se autentica via `/api/v1/auth/authenticate` (obtendo um token JWT).
2. O porteiro registra uma nova encomenda via endpoint `/api/encomendas` (enviando o token no cabeçalho "Authorization").
3. O sistema associa a encomenda a um usuário (morador) e a um compartimento (se disponível).
4. O compartimento é marcado como ocupado.
5. Uma notificação é enviada (ou registrada) para o morador.
6. Um registro de auditoria é criado (via `/api/auditoria`).

### Notificação
- Enviada automaticamente ao morador ao receber uma encomenda.
- Pode ser consultada (ou removida) via API (endpoints em `/api/notificacoes`).

### Auditoria
- Todas as ações relevantes (cadastro, remoção, etc.) são registradas (endpoints em `/api/auditoria`).
- Consultas disponíveis (requer permissão ADMIN).

### Gerenciamento de Compartimentos
- Compartimentos podem ser consultados, criados ou removidos (endpoints em `/api/compartimentos`).
- O estado de ocupação é atualizado conforme o uso.

---

## Testando a API com o Postman

### Autenticação e obtenção do token JWT

- **Endpoint:** `POST /api/v1/auth/authenticate`
- **Body (JSON):**
  ```json
  {
    "email": "porteiro@exemplo.com",
    "senha": "senha123"
  }
  ```
- **No Postman:**  
  - Selecione o método `POST` e insira a URL (ex: `http://localhost:8081/api/v1/auth/authenticate`).
  - Vá em "Body" > "raw" > selecione "JSON" e cole o JSON acima.
  - Clique em "Send".
  - O token JWT virá na resposta, no campo `"token"`.

### Acessando endpoints protegidos (ex: cadastrar encomenda)

- **Endpoint:** `POST /api/encomendas`
- **Headers:**  
  - `Authorization: Bearer SEU_TOKEN_AQUI` (substitua "SEU_TOKEN_AQUI" pelo token obtido na autenticação)
  - `Content-Type: application/json`
- **Body (JSON):**
  ```json
  {
    "idEncomenda": "123",
    "descricao": "Caixa Amazon",
    "remetente": "Amazon",
    "dataRecebimento": "2023-01-01T12:00:00",
    "idArmario": { ... },
    "idUsuario": { ... }
  }
  ```
- **No Postman:**  
  - Cole o token JWT (ex: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") no campo "Authorization".
  - Preencha o body conforme o exemplo.
  - Clique em "Send".

---

## Exemplo de Uso (Cadastro de Encomenda)

### 1. Autenticar (Porteiro)
**Requisição:**
```http
POST /api/v1/auth/authenticate
Content-Type: application/json

{
  "email": "porteiro@exemplo.com",
  "senha": "senha123"
}
```
**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 2. Cadastrar Encomenda (Enviando o Token)
**Requisição:**
```http
POST /api/encomendas
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "idEncomenda": "123",
  "descricao": "Caixa Amazon",
  "remetente": "Amazon",
  "dataRecebimento": "2023-01-01T12:00:00",
  "idArmario": { ... },
  "idUsuario": { ... }
}
```
**Resposta (Encomenda criada):**
```json
{
  "idEncomenda": "123",
  "descricao": "Caixa Amazon",
  "remetente": "Amazon",
  "dataRecebimento": "2023-01-01T12:00:00",
  "idArmario": { ... },
  "idUsuario": { ... }
}
```

---

## Possíveis Extensões

- **Integração com SMS:** Pode ser feita via serviços externos (ex: Twilio, AWS SNS).
- **Interface Web/Mobile:** As APIs já estão preparadas para consumo por frontends (React, Angular, Flutter, etc).
- **Expansão de entidades:** Possível criar repositórios e serviços adicionais (ex: TipoUsuario, etc).

---

## Suporte e Contribuições

- Em caso de dúvidas ou sugestões, abra uma issue no repositório.
- Pull requests são bem-vindos (siga as boas práticas de código e testes).
