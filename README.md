<p align="center">
  <a href="#" target="_blank">
  <img src="https://raw.githubusercontent.com/joaogabriel43/gerenciador-pedidos/main/.github/assets/logo.jpg" alt="Project Logo" width="120">
  </a>
</p>

<h1 align="center">Gerenciador de Pedidos API</h1>

<p align="center">
  Uma API RESTful robusta para gerenciamento de um sistema de pedidos, construída com Java, Spring Boot e as melhores práticas de arquitetura de software.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue.svg" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.1-brightgreen.svg" alt="Spring Boot 3.3.1">
  <img src="https://img.shields.io/badge/Maven-3.9.6-red.svg" alt="Maven">
  <a href="https://github.com/joaogabriel43/gerenciador-pedidos/actions/workflows/ci.yml">
    <img src="https://github.com/joaogabriel43/gerenciador-pedidos/actions/workflows/ci.yml/badge.svg" alt="CI Status">
  </a>
  <a href="https://codecov.io/gh/joaogabriel43/gerenciador-pedidos">
    <img src="https://codecov.io/gh/joaogabriel43/gerenciador-pedidos/branch/main/graph/badge.svg" alt="Code Coverage">
  </a>
  <a href="LICENSE.md">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License: MIT">
  </a>
</p>

<p align="center">
  <a href="#-sobre-o-projeto">Sobre</a> •
  <a href="#-arquitetura-e-decisões">Arquitetura</a> •
  <a href="#-tecnologias">Tecnologias</a> •
  <a href="#-como-executar">Execução</a> •
  <a href="#-endpoints-da-api">API</a> •
  <a href="#-como-contribuir">Contribuir</a>
</p>

---

## 📖 Sobre o Projeto

Este projeto é uma **API RESTful** completa para um sistema de gerenciamento de pedidos, servindo como um portfólio prático de engenharia de backend moderna. Ele permite a gestão do ciclo de vida de **Produtos, Categorias, Fornecedores e Pedidos**, com foco em código limpo, testável e uma arquitetura robusta.

---

## 🏛️ Arquitetura e Decisões

[cite_start]A estrutura do projeto segue uma filosofia de design que prioriza a clareza, manutenibilidade e as melhores práticas da indústria. [cite: 4, 809]

- **Arquitetura em Camadas (Separation of Concerns):** Aderimos a uma estrita separação de responsabilidades. [cite_start]`Controllers` lidam apenas com a camada HTTP, `Services` orquestram a lógica de negócio e `Repositories` gerenciam o acesso a dados. [cite: 1754, 1826] Essa separação impede o acoplamento e torna o sistema mais fácil de entender e modificar.
- **Padrão DTO (Data Transfer Object):** Utilizamos DTOs para criar um contrato claro para a API, desacoplando a camada web das entidades de domínio e fornecendo uma camada extra de segurança contra vulnerabilidades de "Mass Assignment".
- **Tratamento de Exceções Centralizado:** Um `@ControllerAdvice` global captura exceções customizadas (`ResourceNotFoundException`, `BusinessRuleException`), garantindo que a API retorne respostas de erro HTTP padronizadas e consistentes.
- **Validação e Normalização de Dados:** Implementamos lógica de normalização de dados na camada de persistência (`@PrePersist`, `@PreUpdate`) para evitar a duplicidade de informações (ex: "Eletrônicos" e "eletronicos"), garantindo a integridade dos dados.
- [cite_start]**Qualidade Garantida por Testes:** Acreditamos que testes são uma parte integral do desenvolvimento. [cite: 814] A suíte de testes de unidade com **JUnit 5 e Mockito** valida cada peça da lógica de negócio de forma isolada, permitindo refatorações seguras e desenvolvimento ágil.

---

## 🛠️ Tecnologias

| Área | Tecnologia |
| :--- | :--- |
| Linguagem & Framework | **Java 17 (LTS)**, **Spring Boot 3.3.1** |
| Persistência de Dados | **Spring Data JPA**, **Hibernate** |
| Banco de Dados | **PostgreSQL** (Desenvolvimento), **H2** (Testes) |
| Build & Dependências | **Apache Maven** |
| Testes | **JUnit 5**, **Mockito** |

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

- [Java Development Kit (JDK)](https://adoptium.net/) - **Versão 17 (LTS)**.
- [Apache Maven](https://maven.apache.org/download.cgi) - Para gerenciamento do projeto.
- [PostgreSQL](https://www.postgresql.org/download/) - Banco de dados.
- Uma ferramenta de cliente de API, como [Postman](https://www.postman.com/) ou [Insomnia](https://insomnia.rest/).

### 1. Preparação do Banco de Dados
Crie um banco de dados no PostgreSQL com o nome `gerenciador-pedidos`.

### 2. Configuração
Configure as credenciais do seu banco de dados no arquivo `src/main/resources/application.properties`.

### 3. Executando a Aplicação
```bash
# Clone o repositório
git clone [https://github.com/joaogabriel43/gerenciador-pedidos.git](https://github.com/joaogabriel43/gerenciador-pedidos.git)
cd gerenciador-pedidos

# Compile e execute os testes
mvn clean install

# Rode a aplicação
mvn spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

---

<details>
<summary><strong>✅ Endpoints da API (Clique para expandir)</strong></summary>

### Produtos

| Método | URL                 | Descrição                               |
| :----- | :------------------ | :---------------------------------------- |
| `GET`    | `/api/produtos`     | Lista todos os produtos.                  |
| `GET`    | `/api/produtos/{id}`| Busca um produto por ID.                  |
| `POST`   | `/api/produtos`     | Cria um novo produto.                     |
| `PUT`    | `/api/produtos/{id}`| Atualiza um produto existente.            |
| `DELETE` | `/api/produtos/{id}`| Deleta um produto.                        |

_Corpo para `POST` / `PUT`: `{ "nome": "Mouse Gamer", "preco": 250.0, "categoriaId": 1, "fornecedorId": 1 }`_

### Categorias

| Método | URL                  | Descrição                               |
| :----- | :------------------- | :---------------------------------------- |
| `GET`    | `/api/categorias`      | Lista todas as categorias.              |
| `GET`    | `/api/categorias/{id}` | Busca uma categoria por ID.               |
| `POST`   | `/api/categorias`      | Cria uma nova categoria.                  |
| `PUT`    | `/api/categorias/{id}` | Atualiza uma categoria.                   |
| `DELETE` | `/api/categorias/{id}` | Deleta uma categoria (se não estiver em uso). |

_Corpo para `POST` / `PUT`: `{ "nome": "Periféricos" }`_

*(As seções para Fornecedores e Pedidos seguem o mesmo padrão)*

</details>

---

## 🌱 Como Contribuir

Contribuições são a força vital de projetos open source e são **extremamente bem-vindas!**

[cite_start]Este projeto adere ao **Contributor Covenant** como seu Código de Conduta. [cite: 2401] Espera-se que todos na comunidade sigam estas diretrizes.

Para contribuir, por favor, leia nosso **[GUIA DE CONTRIBUIÇÃO](CONTRIBUTING.md)**, que detalha nosso processo de desenvolvimento, padrões de commit e fluxo de Pull Request.

---

## 📄 Licença

Distribuído sob a Licença MIT. Veja o arquivo `LICENSE` para mais informações.

---

## ✒️ Autor

**João Gabriel Borba do Nascimento**

- **LinkedIn:** [https://www.linkedin.com/in/joão-gabriel-borba/](https://www.linkedin.com/in/jo%C3%A3o-gabriel-borba/)
- **GitHub:** [https://github.com/joaogabriel43](https://github.com/joaogabriel43)