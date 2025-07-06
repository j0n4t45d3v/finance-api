# 💰 Finance API

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring--Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)


**Finance API** é uma aplicação backend voltada para **organização financeira pessoal**, permitindo que o usuário tenha **maior controle e visibilidade sobre sua vida financeira**.  
Com ela, é possível entender de forma simples e objetiva **de onde vem e para onde vai seu dinheiro**.

---

## 📐 Arquitetura

A aplicação segue os princípios da **Arquitetura Hexagonal (Ports & Adapters)**, promovendo desacoplamento entre camadas e alta testabilidade.

```
├── docker/
│   ├── docker-compose.yml
│   └── nginx/
│       └── nginx.conf
├── Dockerfile
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── br/
        │       └── com/
        │           └── managementfinanceapi/
        │               ├── adapter/
        │               │   ├── in/
        │               │   │   ├── controller/
        │               │   │   └── dto/
        │               │   └── out/
        │               │       ├── dto/
        │               │       ├── entity/
        │               │       ├── mapper/
        │               │       ├── report/
        │               │       │   └── xlsx/
        │               │       ├── repository/
        │               │       └── security/
        │               │           └── jwt/
        │               ├── application/
        │               │   ├── core/
        │               │   │   ├── domain/
        │               │   │   └── usecase/
        │               │   └── port/
        │               │       ├── in/
        │               │       └── out/
        │               ├── infra/
        │               │   ├── bean/
        │               │   └── security/
        │               └── ManagementFinanceApiApplication.java
        └── resources/
            ├── application-dev.yaml
            ├── application-prod.yaml
            ├── application.yaml
            ├── db/
            │   └── migration/
            │       ├── V2__initial_scheme.sql
            │       ├── V3__add_field_credit_limit_in_category.sql
            │       └── V4__create_table_balances.sql
            └── META-INF/
                ├── orm-balance.xml
                └── orm-category.xml
```

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Maven** – Gerenciador de dependências
- **Spring Boot 3**
  - Spring Security
  - Spring Data JPA
- **Flyway** – Versionamento do banco de dados
- **Swagger/OpenAPI** – Documentação da API
- **JWT (JSON Web Token)** – Autenticação com Access/Refresh Tokens
- **Apache POI** – Geração de relatórios em Excel
- **PostgreSQL** – Banco de dados relacional
- **JUnit 5** – Testes unitários
- **Docker** – Containerização da aplicação
- **Nginx** – Proxy reverso e balanceamento de carga

---

## 🧠 Conceitos Aplicados

- Princípios **SOLID**
- Arquitetura **Hexagonal**
- Autenticação com **Access/Refresh Tokens**
- **Testes unitários** com cobertura
- **Load Balancing** e **Reverse Proxy** com Nginx

---

## ⚙️ Como Rodar o Projeto

1. **Clone o repositório e acesse o diretório:**

```bash
git clone https://github.com/j0n4t45d3v/finance-api.git
cd finance-api/
```
2. **Configure as variáveis de ambiente:**
```bash
cp docker/.env.example docker/.env
```
> Edite o arquivo .env conforme necessário (como usuário do banco, senha, porta etc.)
3. **Inicie o projeto com Docker:**
```
cd docker/
docker-compose --env-file=.env up -d --build
```
4. **Acesse a documentação da API em:** `http://localhost/api/`

---

## 📌 Funcionalidades
- [x] Cadastro e autenticação de usuários
- [x] Registro de receitas e despesas
- [x] Cálculo automático de saldo mensal
- [x] Recalculo de saldos futuros quando transações retroativas são adicionadas
- [x] Geração de relatórios financeiros (.xlsx)
- [x] Swagger UI para explorar as rotas da API

---

## 🧑‍💻 Autor
Desenvolvido por [@j0n4t45d3v](https://github.com/j0n4t45d3v)
