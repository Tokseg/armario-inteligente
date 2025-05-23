# Armário Inteligente

## Descrição Geral

O Armário Inteligente é um sistema para gerenciamento de armários destinados ao recebimento de encomendas em condomínios. Permite o registro, notificação e auditoria de entregas, integrando diferentes perfis de usuários (morador, porteiro, administrador) e mantendo o controle dos compartimentos do armário.

## Requisitos Técnicos

- Java 17 ou superior
- Maven 3.6 ou superior
- Spring Boot 3.x
- PostgreSQL 12 ou superior
- IDE compatível com Java (recomendado: IntelliJ IDEA ou Eclipse)

## Instalação e Configuração

1. Clone o repositório:
```bash
git clone https://github.com/Tokseg/armario-inteligente.git
cd armario-inteligente
```

2. Configure o banco de dados:
   - Crie um banco de dados PostgreSQL
   - Configure as credenciais no arquivo `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/armario_inteligente
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

3. Compile o projeto:
```bash
mvn clean install
```

4. Execute a aplicação:
```bash
mvn spring-boot:run
```

A aplicação estará disponível em `http://localhost:8080`

---

## Estrutura do Projeto

```
src/
 └── main/
      └── java/
           └── br/com/unit/tokseg/armario_inteligente/
                ├── controller/
                ├── model/
                ├── repository/
                └── service/
```

---

## Camadas do Sistema

### 1. Model (Entidades)

#### Usuario
- **Atributos:**
  - `idUsuario` (Long): identificador único.
  - `nome` (String): nome do usuário.
  - `email` (String): e-mail único.
  - `senha` (String): senha (não exposta em respostas).
  - `telefone` (String): telefone de contato.
  - `tipoUsuario` (TipoUsuario): relacionamento N:1 com TipoUsuario.
- **Relacionamentos:** Muitos usuários para um tipo de usuário.

#### TipoUsuario
- **Atributos:**
  - `id` (Long): identificador.
  - `nome` (String): nome do tipo (ex: morador, porteiro).
  - `descricao` (String): descrição do tipo.
  - `usuarios` (List<Usuario>): lista de usuários deste tipo.

#### Compartimento
- **Atributos:**
  - `idCompartimento` (Long): identificador.
  - `armario` (Armario): armário ao qual pertence.
  - `ocupado` (boolean): indica se está ocupado.
  - `encomendaAtual` (Encomenda): encomenda atualmente armazenada.

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
  - `encomendaAtual` (Encomenda): encomenda armazenada.

---

### 2. Repository

Todos os repositórios estendem `JpaRepository`, fornecendo métodos CRUD prontos.

- **UsuarioRepository:** CRUD para Usuario.
- **TipoUsuarioRepository:** (não presente, mas sugerido para expansão).
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

#### NotificacaoController
- **GET /api/notificacoes**  
  Lista todas as notificações.
- **GET /api/notificacoes/{id}**  
  Busca notificação por ID.
- **POST /api/notificacoes**  
  Cria uma nova notificação.  
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
  Remove notificação por ID.

#### RegistroAuditoriaController
- **GET /api/auditoria**  
  Lista todos os registros de auditoria.
- **GET /api/auditoria/{id}**  
  Busca registro por ID.
- **POST /api/auditoria**  
  Cria novo registro de auditoria.
- **DELETE /api/auditoria/{id}**  
  Remove registro por ID.

#### CompartimentoController
- **GET /api/compartimentos**  
  Lista todos os compartimentos.
- **GET /api/compartimentos/{id}**  
  Busca compartimento por ID.
- **POST /api/compartimentos**  
  Cria novo compartimento.
- **DELETE /api/compartimentos/{id}**  
  Remove compartimento por ID.

#### EncomendaController
- **GET /api/encomendas**  
  Lista todas as encomendas.
- **GET /api/encomendas/{id}**  
  Busca encomenda por ID.
- **POST /api/encomendas**  
  Cria nova encomenda.
- **DELETE /api/encomendas/{id}**  
  Remove encomenda por ID.

---

## Fluxos Principais

### Cadastro de Encomenda
1. Porteiro registra uma encomenda via endpoint `/api/encomendas`.
2. Sistema associa encomenda a um usuário e compartimento.
3. Compartimento é marcado como ocupado.
4. Notificação é enviada ao usuário.
5. Registro de auditoria é criado.

### Notificação
- Enviada automaticamente ao usuário ao receber encomenda.
- Pode ser consultada ou removida via API.

### Auditoria
- Todas as ações relevantes (cadastro, remoção, etc.) são registradas.
- Consultas disponíveis via `/api/auditoria`.

### Gerenciamento de Compartimentos
- Compartimentos podem ser consultados, criados ou removidos.
- Estado de ocupação é atualizado conforme uso.

---

## Observações e Possíveis Extensões

- **Segurança:** Não há autenticação implementada, mas pode ser adicionada via Spring Security.
- **Integração com SMS:** Pode ser feita via serviços externos.
- **Interface Web/Mobile:** APIs já preparadas para consumo por frontends.
- **Expansão de entidades:** Possível criar repositórios e serviços para `TipoUsuario` e outros.

---

## Exemplo de Uso (Cadastro de Encomenda)

**Requisição:**
```http
POST /api/encomendas
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

**Resposta:**
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

## Segurança

O sistema implementa as seguintes medidas de segurança:
- Autenticação via JWT (JSON Web Token)
- Senhas armazenadas com hash bcrypt
- Validação de entrada de dados
- Proteção contra SQL Injection (via JPA/Hibernate)
- CORS configurado para origens específicas

## Testes

O projeto inclui testes unitários e de integração. Para executar os testes:

```bash
mvn test
```

### Configuração do Ambiente de Teste

Para executar os testes corretamente, certifique-se de que:

1. O banco de dados PostgreSQL está rodando
2. As credenciais de teste estão configuradas em `src/test/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/armario_inteligente_test
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=create-drop
```

## Logs e Monitoramento

- Logs são gerados usando SLF4J com Logback
- Níveis de log configuráveis em `application.properties`
- Logs são salvos em `logs/application.log`

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## Suporte

Para suporte, entre em contato através de:
- Email: hugo.mramos@souunit.com.br
- Issues do GitHub: [Link para issues](https://github.com/Tokseg/armario-inteligente/issues)
