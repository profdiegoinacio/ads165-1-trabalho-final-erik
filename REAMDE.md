# Conecta Pro (ads165-1-trabalho-final-erik)


 Tecnologias Utilizadas

* **Backend:**
    * Java 21
    * Spring Boot 3.x
    * Spring Data JPA (Hibernate)
    * Spring Security
    * PostgreSQL (Banco de Dados)
    * Gradle (Build Tool com Kotlin DSL)
    * Lombok
    * JWT (JSON Web Tokens) para autenticação (via biblioteca `java-jwt` Auth0)
* **Frontend:**
    * Node.js
    * Next.js 14+ (App Router)
    * React 18+
    * TypeScript
    * Tailwind CSS (Estilização)
    * Axios (Cliente HTTP)
    * ESLint (Linting)
* **Ambiente:**
    * IDE: IntelliJ IDEA (sugerido para backend), VS Code (sugerido para frontend)
    * Banco de Dados: Instância PostgreSQL local ou Dockerizada.
    * Gerenciador de Pacotes Frontend: npm ou yarn.

### Estrutura do Projeto

O repositório está organizado em duas pastas principais:

* `backend/`: Contém o código da API Spring Boot.
* `frontend/`: Contém o código da aplicação Next.js.

 Configuração e Execução

Siga os passos abaixo para configurar e executar ambas as partes da aplicação localmente.

### Backend (Spring Boot)

**Pré-requisitos:**

* JDK 21 (ou superior) instalado e configurado no `PATH`.
* Gradle instalado (opcional, pois o projeto inclui o Gradle Wrapper - `gradlew`).
* Uma instância do PostgreSQL rodando localmente ou acessível pela rede.

**Passos:**

1.  **Configurar Banco de Dados PostgreSQL:**
    * Certifique-se que o servidor PostgreSQL esteja rodando.
    * Crie um banco de dados dedicado para a aplicação (ex: `conectapro_db`). Use um cliente como DBeaver, pgAdmin ou `psql`.
        ```sql
        -- Exemplo usando psql:
        CREATE DATABASE conectapro_db;
        -- Cria um banco de dados com o nome de conectapro_db
        -- Crie um usuário ou use um existente (ex: postgres)
        ```
2.  **Configurar `application.properties`:**
    * Navegue até `backend/src/main/resources/`.
    * Abra o arquivo `application.properties`.
    * Ajuste as seguintes propriedades com os dados da sua instância PostgreSQL:
        ```properties
        spring.datasource.url=jdbc:postgresql://localhost:5432/conectapro_db # Mude host, porta e nome do DB se necessário
        spring.datasource.username=seu_usuario_do_postgres
        spring.datasource.password=sua_senha_do_postgres
        ```
      
        ```
    * (Opcional) Ajuste `api.security.jwt.expiration-ms` se desejar um tempo de expiração diferente para o token (o padrão atual é 2 horas).
    * (Opcional) Verifique `spring.jpa.hibernate.ddl-auto`. O valor `update` é conveniente para desenvolvimento inicial, mas `validate` ou `none` (com migrations via Flyway/Liquibase) é recomendado para produção ou desenvolvimento estável.
3.  **Compilar o Projeto (Build):**
    * Abra um terminal na pasta raiz `backend/`.
    * Execute o comando do Gradle Wrapper:
        * Windows: `gradlew.bat build`
    * Isso baixará as dependências e compilará o código.
4.  **Executar a Aplicação:**
    * **Via Gradle Wrapper:**
        * Windows: `gradlew.bat bootRun`
    * **Via IDE:** Importe o projeto Gradle na sua IDE (IntelliJ) e execute a classe principal `BackendApplication.java`.
    * **Via JAR (após o build):** `java -jar build/libs/backend-*.jar` (substitua `*` pelo nome do JAR gerado).
    * O backend estará rodando, por padrão, em `http://localhost:8080`. Verifique os logs para confirmar a inicialização bem-sucedida e a conexão com o banco.

### Frontend (Next.js)

**Pré-requisitos:**

* Node.js (versão LTS recomendada, ex: 18.x, 20.x) instalado.
* npm ou yarn instalado (geralmente vem com o Node.js).

**Passos:**

1.  **Configurar Variável de Ambiente:**
    * Navegue até a pasta raiz `frontend/`.
    * Crie um arquivo chamado `.env.local` (se não existir).
    * Adicione a seguinte linha, garantindo que a URL aponta para o seu backend rodando:
        ```
        NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
        ```
      *(Ajuste a porta ou URL se seu backend rodar em local diferente)*.
2.  **Instalar Dependências:**
    * Abra um terminal na pasta raiz `frontend/`.
    * Execute o comando:
        * Usando npm: `npm install`
        * Usando yarn: `yarn install`
3.  **Executar a Aplicação em Modo de Desenvolvimento:**
    * No mesmo terminal (na pasta `frontend/`), execute:
        * Usando npm: `npm run dev`
        * Usando yarn: `yarn dev`
    * O frontend estará acessível, por padrão, em `http://localhost:3000`.

## 5. Decisões Arquiteturais

* **Monorepo-like:** O código do backend e frontend residem no mesmo repositório para facilitar o gerenciamento, mas são aplicações independentes.
* **Backend (Spring Boot):**
    * Arquitetura em Camadas (Controller, Service, Repository, Domain, DTO) para separação de responsabilidades.
    * Spring Data JPA para abstração da persistência de dados.
    * Spring Security para gerenciamento de segurança, com autenticação baseada em JWT.
    * Injeção de Dependência via construtor (preferencialmente).
* **Frontend (Next.js):**
    * App Router para estrutura moderna de roteamento e renderização.
    * TypeScript para tipagem estática e robustez.
    * Tailwind CSS para estilização utilitária rápida.
    * Axios para chamadas à API backend, com instância configurada.
    * Componentes de Cliente (`"use client";`) para páginas interativas (formulários, etc.).
    * Componentes reutilizáveis (`Input`, `Button`) para consistência da UI.
    * Armazenamento de token JWT no `localStorage` (simples para começar, considerar alternativas mais seguras como cookies HttpOnly se necessário).

## 6. Endpoints da API Implementados

A URL base da API é: `http://localhost:8080/api/v1`

### Autenticação

* **`POST /auth/login`**
    * **Descrição:** Autentica um usuário existente e retorna um token JWT.
    * **Request Body:**
        ```json
        {
          "login": "nome_do_usuario",
          "senha": "senha_do_usuario"
        }
        ```
    * **Success Response (200 OK):**
        ```json
        {
          "token": "SEU_TOKEN_JWT_AQUI",
          "tipo": "Bearer"
        }
        ```
    * **Error Response (401 Unauthorized):** Se as credenciais forem inválidas.

### Usuários

* **`POST /usuarios/registrar`**
    * **Descrição:** Registra um novo usuário.
    * **Request Body:**
        ```json
        {
          "nome": "Nome Completo",
          "email": "email@exemplo.com",
          "nomeUsuario": "nome_usuario_unico",
          "telefone": "(Opcional) 11912345678",
          "senha": "senha_forte"
        }
        ```
    * **Success Response (201 Created):**
        * Header `Location`: URL do novo usuário (ex: `/api/v1/usuarios/1`)
        * Body: Objeto do usuário criado (sem a senha).
    * **Error Response (400 Bad Request):** Se dados forem inválidos, ou email/nome de usuário já existirem.

* **`GET /usuarios`**
    * **Descrição:** Lista todos os usuários registrados.
    * **Autenticação:** Requerida (Token JWT no header `Authorization: Bearer <token>`).
    * **Success Response (200 OK):** Array de objetos de usuário (sem senha).
    * **Success Response (204 No Content):** Se não houver usuários.
    * **Error Response (401 Unauthorized / 403 Forbidden):** Se não autenticado ou sem permissão.

* **`GET /usuarios/{id}`**
    * **Descrição:** Busca um usuário específico pelo seu ID.
    * **Autenticação:** Requerida.
    * **Success Response (200 OK):** Objeto do usuário encontrado (sem senha).
    * **Error Response (404 Not Found):** Se o usuário com o ID especificado não existir.
    * **Error Response (401 Unauthorized / 403 Forbidden):** Se não autenticado ou sem permissão.

## 7. Próximos Passos (TODO)

* Implementar endpoints PUT/DELETE para usuários.
* Implementar gerenciamento de perfil do usuário (editar dados, foto, etc.).
* Adicionar sistema de Papéis (Roles) e permissões mais granulares.
* Criar DTOs de resposta para controlar os dados expostos pela API.
* Proteger a rota `/dashboard` no frontend.
* Implementar o serviço de API (`authService.ts`) no frontend para encapsular as chamadas Axios.
* Refatorar para usar Context API ou outra lib de estado global no frontend para gerenciar autenticação.
* Implementar as demais funcionalidades (busca, avaliações, chat, etc.).
* Configurar CORS de forma mais segura para produção.
* Implementar testes unitários e de integração.

---