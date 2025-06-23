# Conecta Pro

Conecta Pro é uma plataforma digital no formato de rede social projetada para facilitar a conexão entre profissionais qualificados e clientes. O objetivo é criar um ecossistema robusto onde prestadores de serviço possam ser encontrados, avaliados e contratados, e onde clientes possam encontrar com segurança os talentos que precisam.

## Funcionalidades Atuais

A aplicação possui uma base sólida e funcional, implementando o ciclo completo de interação de uma rede social profissional:

* **Autenticação e Segurança:**
    * Sistema completo de cadastro, login e gerenciamento de sessão com NextAuth.js.
    * Backend protegido com Spring Security e autenticação baseada em tokens JWT (JSON Web Tokens).

* **Perfis de Usuário:**
    * Criação e edição de perfis detalhados, incluindo nome, bio, formação e URLs para fotos de perfil e capa.
    * Distinção visual e lógica entre usuários comuns e perfis profissionais.

* **Interação Social:**
    * Sistema de **Seguir / Deixar de Seguir** entre usuários.
    * **Feed de Postagens:** Usuários podem criar postagens de texto. O feed principal exibe as postagens em ordem cronológica.
    * **Sistema de Avaliações:** Usuários podem avaliar profissionais com uma nota (de 1 a 5 estrelas) e um comentário. A nota média é exibida dinamicamente no perfil.

* **Busca e Descoberta:**
    * **Busca Geral:** Campo de busca funcional que encontra usuários por nome ou nome de usuário.
    * **Filtro por Categorias:** Uma página dedicada permite que os usuários cliquem em uma área de atuação (ex: "Desenvolvimento Web") e vejam uma lista de todos os profissionais pertencentes àquela categoria.

## 🛠️ Tecnologias Utilizadas

| Backend (API)                                | Frontend (Interface)                    |
| -------------------------------------------- | --------------------------------------- |
| Java 21 & Spring Boot 3.x                    | Next.js 15+ (App Router) & React        |
| Spring Security (com JWT)                    | TypeScript                              |
| Spring Data JPA (Hibernate)                  | NextAuth.js v5 (Autenticação)           |
| PostgreSQL                                   | Tailwind CSS (Estilização)              |
| Gradle                                       | Axios (Cliente HTTP)                    |
| Lombok                                       | Lucide React (Ícones)                   |

## ⚙️ Como Executar o Projeto Localmente

Siga os passos abaixo para ter a aplicação completa rodando na sua máquina.

### Pré-requisitos

* **Java (JDK) 21** ou superior.
* **Node.js** (versão LTS, ex: 18.x ou 20.x).
* **PostgreSQL** instalado e com um servidor ativo.

---

### 1. Configuração do Backend

1.  **Crie o Banco de Dados:**
    * No seu PostgreSQL, crie um novo banco de dados. O nome padrão usado no projeto é `conectapro_db`.

2.  **Configure as Propriedades:**
    * Navegue até a pasta `backend/`.
    * Abra o arquivo `src/main/resources/application.properties`.
    * Ajuste as credenciais do seu PostgreSQL.

      ```properties
      # Conexão com o Banco
      spring.datasource.url=jdbc:postgresql://localhost:5432/conectapro_db
      spring.datasource.username=seu_usuario_postgres
      spring.datasource.password=sua_senha_postgres
      
      # Ativa o perfil 'dev' para criar usuários de teste na primeira inicialização
      spring.profiles.active=dev
      ```

3.  **Execute o Backend:**
    * Abra um terminal na pasta raiz `backend/`.
    * Rode o comando:
        * No Windows: `gradlew.bat bootRun`
        * No Linux/macOS: `./gradlew bootRun`
    * O backend estará rodando em `http://localhost:8080`.
    * **Usuários de Teste:** Com o perfil `dev`, os usuários `admin` (senha: `admin123`) e `user` (senha: `user123`) serão criados automaticamente.

---

### 2. Configuração do Frontend

1.  **Configure as Variáveis de Ambiente:**
    * Navegue até a pasta `frontend/`.
    * Crie um arquivo chamado `.env.local` na raiz desta pasta.
    * Adicione as seguintes variáveis:

      ```
      # URL da API Backend
      NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1

      # URL da sua aplicação frontend
      AUTH_URL=http://localhost:3000

      # Chave secreta para o NextAuth.js
      # Esta chave DEVE SER COMPARTILHADA com o backend.
      AUTH_SECRET=SUA_CHAVE_SECRETA_UNICA_AQUI
      ```
    * **Aviso Importante:** A chave `AUTH_SECRET` aqui e a `api.security.jwt.secret` no backend **DEVEM TER EXATAMENTE O MESMO VALOR**.

2.  **Sincronize a Chave Secreta com o Backend:**
    * Abra novamente o arquivo `backend/src/main/resources/application.properties`.
    * Encontre a propriedade `api.security.jwt.secret`.
    * **Cole o mesmo valor** que você usou para `AUTH_SECRET` no frontend.

      ```properties
      # Chave secreta para o JWT
      api.security.jwt.secret=SUA_CHAVE_SECRETA_UNICA_AQUI 
      ```
    * *Dica para gerar uma chave segura no terminal: `openssl rand -base64 32`*


3.  **Instale as Dependências:**
    * Abra um terminal na pasta raiz `frontend/`.
    * Rode: `npm install`

4.  **Execute o Frontend:**
    * No mesmo terminal, rode: `npm run dev`
    * A aplicação estará acessível em `http://localhost:3000`.

## 📖 Endpoints Principais da API

A URL base da API é `http://localhost:8080/api/v1`.

| Método | Endpoint                        | Descrição                                        | Autenticação? |
| :----- | :------------------------------ | :----------------------------------------------- | :------------ |
| `POST` | `/auth/login`                   | Autentica um usuário e retorna um token JWT.     | Não           |
| `POST` | `/usuarios/registrar`           | Registra um novo usuário.                        | Não           |
| `GET`  | `/postagens`                    | Lista as postagens do feed principal.            | Não           |
| `GET`  | `/usuarios/{username}/perfil`   | Busca os dados do perfil de um usuário.          | Não           |
| `GET`  | `/areas-de-atuacao`             | Lista todas as áreas de atuação disponíveis.     | Não           |
| `POST` | `/postagens`                    | Cria uma nova postagem.                          | **Sim** |
| `PUT`  | `/usuarios/perfil`              | Atualiza o perfil do usuário autenticado.        | **Sim** |
| `POST` | `/usuarios/{user}/seguir`       | Segue o usuário especificado.                    | **Sim** |
| `POST` | `/usuarios/{user}/deixar-de-seguir` | Deixa de seguir o usuário especificado.          | **Sim** |
| `POST` | `/avaliacoes`                   | Cria uma nova avaliação para um usuário.         | **Sim** |

## 🔮 Próximos Passos

* [ ] **Feed "Seguindo":** Alterar o feed principal para mostrar um feed cronológico apenas com postagens de usuários que o usuário logado segue.
* [ ] **Upload de Imagens:** Implementar um sistema de upload de arquivos para as fotos de perfil e capa (ex: para Cloudinary ou S3), em vez de usar URLs.
* [ ] **Notificações:** Criar um sistema de notificações para eventos como "novo seguidor", "nova avaliação", "curtida no post", etc.
* [ ] **Chat / Mensagens Diretas:** Implementar uma funcionalidade de chat em tempo real entre usuários.
* [ ] **Testes:** Adicionar testes unitários (JUnit, Mockito) e de integração para garantir a estabilidade e a qualidade do código.
* [ ] **Refatoração e Otimização:** Melhorar a UI/UX, otimizar queries do banco de dados e refatorar componentes conforme a aplicação cresce.

