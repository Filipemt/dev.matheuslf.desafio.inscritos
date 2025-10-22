# API de Gerenciamento de Projetos e Tarefas

Esta é uma API RESTful para gerenciar projetos e suas tarefas associadas, construída com Spring Boot. A aplicação fornece endpoints para criar, consultar, atualizar e deletar projetos e tarefas, com um sistema de autenticação seguro baseado em JWT.

---

## Funcionalidades

- **Autenticação de Usuários**: Registro e login seguros utilizando JWT (JSON Web Tokens).
- **Gerenciamento de Projetos**: Criação e listagem de projetos.
- **Gerenciamento de Tarefas**: Criação, listagem (com filtros), atualização e exclusão de tarefas associadas a um projeto.
- **Documentação da API**: Documentação interativa da API disponível via Swagger UI.
- **Tratamento de Exceções Centralizado**: Respostas de erro padronizadas para uma experiência de API consistente.

---


- **Backend**: Java 17, Spring Boot 3
## Tecnologias Utilizadas
- **Banco de Dados**: H2 InMemory Database
- **Segurança**: Spring Security, JWT
- **Documentação da API**: Springdoc (Swagger UI)
- **Ferramenta de Build**: Maven
- **Testes**: JUnit 5, Mockito, MockMvc

---

## Pré-requisitos

Antes de começar, garanta que você tenha o seguinte instalado:
- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou uma versão superior.
- [Apache Maven](https://maven.apache.org/download.cgi)
- Um cliente REST como [Postman](https://www.postman.com/downloads/) ou [Insomnia](https://insomnia.rest/download) para interagir com a API.

---

## Como Rodar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone <https://github.com/Filipemt/dev.matheuslf.desafio.inscritos>
    cd dev.matheuslf.desafio.inscritos
    ```

2.  **Compile o projeto com o Maven:**
    ```bash
    mvn clean install
    ```

3.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```

4.  **Acessando a aplicação:**
    - A API estará disponível em `http://localhost:8080`.
    - O console do banco de dados H2 pode ser acessado em `http://localhost:8080/h2-console`.
      - **JDBC URL**: `jdbc:h2:mem:sistema-gestao-projetos-demandas`
      - **Username**: `sa`
      - **Password**: (deixe em branco)

---

## Utilizando a API

### Documentação da API (Swagger UI)

Após iniciar a aplicação, você pode acessar a documentação interativa do Swagger UI para visualizar e testar todos os endpoints disponíveis:

- **URL**: `http://localhost:8080/swagger-ui.html`

### Fluxo de Autenticação

1.  **Registre um novo usuário** enviando uma requisição `POST` para `/auth/register`.
2.  **Faça o login** enviando uma requisição `POST` para `/auth/login` com as credenciais registradas. Você receberá um token JWT na resposta.
3.  **Autorize as requisições** incluindo o token JWT no cabeçalho `Authorization` para todos os endpoints protegidos (Projetos e Tarefas).
    - **Formato do Cabeçalho**: `Authorization: Bearer <seu-token-jwt>`

---

## Executando os Testes

Este projeto inclui uma suíte completa de testes unitários e de integração para garantir a qualidade e a confiabilidade do código.

Para executar todos os testes, execute o seguinte comando Maven a partir do diretório raiz do projeto:

```bash
mvn test
```
