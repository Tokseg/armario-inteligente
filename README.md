# Armário Inteligente

## Descrição

O projeto consiste em um sistema para gerenciamento de um armário inteligente destinado ao recebimento de pequenas encomendas em portarias de condomínios. O armário notifica o morador sobre a chegada da encomenda via aplicativo ou SMS, e todas as informações ficam registradas em um banco de dados para auditoria.

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

## Camadas

### 1. Controller

Responsável por expor as APIs REST para manipulação dos dados.

- **RegistroAuditoriaController**: Gerencia registros de auditoria (listar, buscar por ID, criar e deletar).
- **NotificacaoController**: Gerencia notificações enviadas aos usuários.
- **CompartimentoController**: Gerencia os compartimentos do armário.
- **EncomendaController**: Gerencia as encomendas recebidas.

### 2. Model

Define as entidades do sistema:

- **Usuario**: Representa o usuário do sistema (morador, porteiro, etc).
- **TipoUsuario**: Define o tipo do usuário (ex: morador, porteiro, administrador).
- **Notificacao**: Representa uma notificação enviada ao usuário.
- **RegistroAuditoria**: Guarda registros de ações para auditoria.
- **Encomenda**: Representa uma encomenda recebida.
- **Armario**: Representa o armário físico.
- **Compartimento**: Representa um compartimento individual do armário.

### 3. Repository

Interfaces para acesso ao banco de dados, utilizando Spring Data JPA.

- **UsuarioRepository**
- **NotificacaoRepository**
- **RegistroAuditoriaRepository**
- **EncomendaRepository**
- **ArmarioRepository**
- **CompartimentoRepository**

### 4. Service

Contém a lógica de negócio do sistema.

- **RegistroAuditoriaService**
- **NotificacaoService**
- **EncomendaService**
- **CompartimentoService**

---

## Funcionamento Básico

- **Cadastro de Encomendas**: Porteiro registra uma nova encomenda, associando-a a um usuário e a um compartimento do armário.
- **Notificação**: O sistema envia uma notificação ao usuário informando sobre a chegada da encomenda.
- **Auditoria**: Todas as ações relevantes são registradas para fins de auditoria.
- **Gerenciamento de Compartimentos**: O sistema controla quais compartimentos estão ocupados ou livres.

---

## Como Executar

O projeto utiliza Spring Boot. Para rodar localmente:

1. Certifique-se de ter o Java e o Maven instalados.
2. Execute o comando:  
   ```
   ./mvnw spring-boot:run
   ```
3. As APIs estarão disponíveis em `http://localhost:8080/api/`.

---

## Observações

- O projeto pode ser expandido para incluir autenticação, integração com serviços de SMS, e interface web/mobile.
- O banco de dados utilizado pode ser configurado no arquivo `application.properties` (não listado aqui).

---

Se desejar uma documentação detalhada de cada classe, método ou endpoint, posso gerar comentários Javadoc diretamente no código ou criar uma documentação técnica mais aprofundada. Informe se deseja esse detalhamento!
