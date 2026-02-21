# FINANCE API

API REST para gestão de finanças pessoais.

Permite o controle de receitas e despesas, categorização de transações,
gerenciamento de contas bancárias e visualização de dados financeiros
através de endpoints de dashboard com métricas consolidadas.

## Funcionalidades

- Cadastro de transações de débito e crédito
- Cadastro de categorias
- Cadastro de contas bancárias
- Dashboard com:
    - Ranking de categorias com maior movimentação
    - Ranking das transações com maior movimentação
    - Últimas transações
    - Resumo financeiro por período
    - Comparativo entre receitas e despesas
    - Agrupamento por dia, mês ou categoria
     
## Tecnologias

- Java 21
- Spring boot
  - Spring Data Jpa 
  - Spring Security 
  - Springdoc OpenApi
- Flyway 
- PostgreSQL 
- Docker
- JUnit 5
- Mockito
 
## Arquitetura e Boas Práticas

- Arquitetura em camadas (Controller → Service → Repository)
- Regras de negócio centralizadas na camada de Service
- Entidades protegidas contra estados inválidos
- Autenticação baseada em JWT (Access + Refresh Token)
- Separação explícita entre tipos de token
- Retorno de erros de negócio como objetos de resultado (sem uso de exceptions para fluxo normal)
- Testes unitários com JUnit 5 e Mockito (em andamento)
- Documentação automática com Springdoc OpenAPI
- Uso correto de métodos HTTP semânticos 
- Padrão REST para estruturar as rotas 
 
## Segurança da Api

- Autenticação com JWT
- Access Token de curta duração
- Refresh Token com rotação
- Validação de assinatura, expiração e tipo do token
- Validação do subject antes de conceder acesso
- Isolamento de dados por usuário (cada requisição é vinculada ao usuário autenticado)
 
## Como Rodar o Projeto

### Requerimentos
- docker 

1. Clone o repositório
```bash
git clone https://github.com/j0n4t45d3v/finance-api.git
cd finance-api/
```
2. Executando o projeto
```bash
docker-compose -f docker/docker-compose.dev.yml up -d
```
> Após rodar o projeto acesse [http://localhost:8080/api/doc](http://localhost:8080/api/doc) para visualizar a documentação
### Exemplo de criação de transação

```http
POST /v1/transactions
Authorization: Bearer {access_token}
Content-Type: application/json
```

```json
{
  "amount": 0.1,
  "datetime": "2026-02-21T13:13:56.418Z",
  "categoryId": 2,
  "accountId": 1
}
```

**Resposta:**
```http
HTTP/1.1 201 CREATED
Location /v1/transactions/<id-generated>
``` 

