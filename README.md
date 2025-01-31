# Sistema de E-Commerce
Este projeto é um sistema básico de E-Commerce. 
Ele permite o cadastro de produtos, clientes e a realização de compras, simulando o 
funcionamento de uma loja virtual, com validações e manipulação de dados. 


## Funcionalidades

### Produtos
- **GET /produtos**: Retorna a lista de todos os produtos cadastrados.
- **POST /produtos**: Cadastra um novo produto.
- Validações:
    - Nome não pode ser repetido.
    - Preço deve ser maior que 0.
    - Quantidade deve ser maior ou igual a 0.

### Clientes
- **POST /clientes**: Cadastra um novo cliente.
  - Validações:
    - Nome é obrigatório.
    - CPF deve ser único e válido.
    - Email deve ser único e válido.
- **GET /clientes/{cpf}**: Retorna os dados de um cliente específico pelo CPF.
- **PUT /clientes/{cpf}**: Atualiza os dados de um cliente pelo CPF.

### Compras
- **POST /compras**: Registra uma nova compra.
  - O cliente é identificado pelo CPF.
  - A quantidade do produto é reduzida no estoque após a compra.
  - Validações:
    - Produtos com quantidade 0 não podem ser comprados.
    - Retorna erro 400 com a mensagem: `{"erro": "Produto em falta: [nome do produto]"}` se o produto estiver indisponível.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot 3.4.1**
- **Maven**
- **H2 Database** (banco de dados em memória para testes)
- **Jakarta Validation** (para validações de dados)

## Arquitetura do Projeto

O projeto segue a arquitetura em camadas, com as seguintes pastas principais:

```plaintext
src/main/java/com/zup/e_commerce
├── controllers                 # Controladores REST
├── dtos                        # Objetos de Transferência de Dados
├── exceptions                  # Tratamento de exceções personalizadas
├── models                      # Entidades de domínio
├── repositories                # Interfaces de repositórios (Spring Data JPA)
├── services                    # Regras de negócio e lógica de aplicação
└── EcommerceApplication.java   # Classe principal
```

## Regras de Negócio
1. **Cadastro de Produtos**:
   - Nome deve ser único.
   - Preço deve ser maior que 0.
   - Quantidade deve ser maior ou igual a 0.
   
2. **Cadastro de Clientes**:
   - CPF deve ser único e válido.
   - Email deve ser único e válido.

3. **Realização de Compras**:
   - Produtos com quantidade 0 não podem ser comprados.
   - A quantidade do produto no estoque é reduzida após a compra.


## Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/eloiza-souza/e-commerce-api.git
   cd e-commerce-api
   ```

2. Compile e execute o projeto:
   ```bash
   ./mvnw spring-boot:run
   ```

3. Acesse a API no endereço:
   ```
   http://localhost:8080
   ```

## Exemplos de Requisições

### Cadastro de Produto
**POST /produtos**
```json
{
  "name": "Produto 1",
  "price": 100.0,
  "quantity": 10
}
```
### Cadastro de Cliente
**POST /clientes**
```json
{
  "name": "João Silva",
  "cpf": "12345678900",
  "email": "joao.silva@email.com"
}
```
### Realização de Compra
**POST /compras**
```json
{
  "cpf": "12345678900",
  "productNames":  [
    "Produto1",
    "Produto2"
  ]
}
```

## Documentação do Swagger 📜

A documentação do Swagger foi configurada para facilitar a interação com a API RESTful do sistema de E-Commerce. Abaixo estão as instruções para acessar e utilizar o Swagger.

### Acessando o Swagger UI 🌐
Após iniciar a aplicação, a documentação interativa do Swagger estará disponível no seguinte endereço:
- **URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

O Swagger UI permite que você visualize e teste os endpoints da API diretamente no navegador.

## Contribuição

Sinta-se à vontade para contribuir com melhorias para este projeto. Para isso:
1. Faça um fork do repositório.
2. Crie uma branch para sua feature:
   ```bash
   git checkout -b minha-feature
   ```
3. Envie um pull request.

## Autor

Desenvolvido por [Eloiza Souza](https://github.com/eloiza-souza).

