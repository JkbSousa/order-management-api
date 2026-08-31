# Order Management Spring

API REST para gestão de pedidos, desenvolvida como projeto de portfólio para praticar arquitetura backend com Spring Boot, JPA/Hibernate e PostgreSQL.

O sistema modela um fluxo completo de e-commerce: clientes (pessoa física ou jurídica), produtos (físicos ou digitais), pedidos com múltiplos itens, pagamentos (cartão, PIX ou boleto) e endereços de entrega.

## Stack

- **Java 25**
- **Spring Boot 4.1**
- **Spring Data JPA / Hibernate**
- **PostgreSQL 18** (rodando via Docker)
- **Bean Validation** (Jakarta Validation)
- **Springdoc OpenAPI** (Swagger UI)
- **Maven**

## Arquitetura

O projeto segue uma separação clássica em camadas:

```
controllers/   → endpoints REST
services/      → regras de negócio
repositories/  → acesso a dados (Spring Data JPA)
entities/      → modelo de domínio (JPA)
dto/           → objetos de request/response, isolando a API do modelo interno
exceptions/    → exceções customizadas + tratamento global de erros
```

### Modelagem com herança

Três entidades usam herança JPA (`SINGLE_TABLE`) para modelar variações de um mesmo conceito:

- **Client**: `IndividualClient` (pessoa física, com CPF) ou `CorporateClient` (pessoa jurídica, com CNPJ)
- **Product**: `PhysicalProduct` (com peso, calcula frete) ou `DigitalProduct` (com link de download, frete gratuito)
- **Payment**: `CardPayment`, `PixPayment` ou `BoletoPayment`, cada um com sua própria lógica de `processPayment()`

Os DTOs de request/response usam um campo `type` para indicar qual subtipo está sendo criado ou retornado, e os campos específicos de cada subtipo só aparecem no JSON quando fazem sentido (via `@JsonInclude(NON_NULL)`).

## Como rodar o projeto

### 1. Suba o PostgreSQL via Docker

```bash
docker run --name postgres-order \
  -e POSTGRES_PASSWORD=senha123 \
  -e POSTGRES_DB=order_management \
  -p 5432:5432 \
  -d postgres
```

### 2. Configure o `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/order_management
spring.datasource.username=postgres
spring.datasource.password=senha123
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Rode a aplicação

Via Maven:
```bash
./mvnw spring-boot:run
```

Ou diretamente pela sua IDE, executando `OrderManagementSpringApplication`.

A API sobe em `http://localhost:8080`.

### 4. Explore pelo Swagger

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

Lá você encontra todos os endpoints documentados, com os DTOs de request e a opção de testar direto pelo navegador.

## Endpoints principais

| Recurso | Método | Rota | Descrição |
|---|---|---|---|
| Clients | GET | `/clients` | Lista todos os clientes |
| Clients | GET | `/clients/{id}` | Busca cliente por id |
| Clients | POST | `/clients` | Cria um cliente |
| Clients | PUT | `/clients/{id}` | Atualiza um cliente |
| Clients | DELETE | `/clients/{id}` | Remove um cliente |
| Products | GET / POST / PUT / DELETE | `/products` | CRUD de produtos |
| Address | GET / POST / PUT / DELETE | `/address` | CRUD de endereços |
| Payment | GET / POST | `/payment` | Consulta e criação de pagamentos |
| Payment | PUT | `/payment/{id}` | Edita um pagamento (somente se `PENDING`) |
| Payment | POST | `/payment/{id}/process` | Processa um pagamento pendente |
| Orders | GET / POST | `/orders` | Consulta e criação de pedidos completos |
| Orders | PUT | `/orders/{id}/status` | Atualiza o status do pedido |
| Orders | PUT | `/orders/{id}/address` | Atualiza o endereço de entrega (bloqueado após envio) |

## Tratamento de erros

Todas as respostas de erro seguem um formato padronizado:

```json
{
  "timestamp": "2026-08-31T11:33:01.32",
  "status": 404,
  "error": "Not Found",
  "message": "Payment not found, id: 9999",
  "details": null
}
```

| Situação | Status |
|---|---|
| Recurso não encontrado | 404 |
| Regra de negócio violada (ex: tipo de pagamento inválido) | 400 |
| Campos inválidos (Bean Validation) | 400, com lista em `details` |
| Erro inesperado | 500 |

## Exemplos de uso

### Criar um pagamento PIX (`POST /payment`)

**Request:**
```json
{
  "type": "PIX",
  "amount": 150.00,
  "orderId": 3,
  "pixKey": "carla@email.com",
  "pixHolderName": "Carla Souza"
}
```

**Response (200):**
```json
{
  "id": 8,
  "type": "PIX",
  "amount": 150.00,
  "status": "PENDING",
  "orderId": 3,
  "pixKey": "carla@email.com",
  "pixHolderName": "Carla Souza"
}
```

### Criar um pedido completo (`POST /orders`)

Suporta tanto um cliente já existente (`clientId`) quanto os dados de um cliente novo (`client`).

**Request:**
```json
{
  "client": {
    "type": "INDIVIDUAL",
    "name": "Carla Souza",
    "email": "carla@email.com",
    "birthDate": "1990-04-15",
    "cpf": "14445677788"
  },
  "items": [
    { "productId": 1, "quantity": 1 }
  ],
  "payment": {
    "type": "PIX",
    "amount": 450.00,
    "pixKey": "carla@email.com",
    "pixHolderName": "Carla Souza"
  },
  "shippingAddress": {
    "street": "Rua das Flores",
    "number": "2",
    "complement": "Casa",
    "neighborhood": "Boa Viagem",
    "city": "Recife",
    "state": "PE",
    "zipCode": "50000000"
  }
}
```

**Response (200):**
```json
{
  "id": 6,
  "moment": "2026-08-31T11:19:31.88",
  "status": "PENDING_PAYMENT",
  "client": {
    "id": 6,
    "type": "INDIVIDUAL",
    "name": "Carla Souza",
    "email": "carla@email.com",
    "birthDate": "1990-04-15",
    "cpf": "14445677788"
  },
  "items": [
    {
      "id": 6,
      "product": {
        "id": 1,
        "type": "PHYSICAL",
        "name": "Micro-ondas",
        "price": 450.00,
        "description": "Micro-ondas 30L",
        "weight": 5.0
      },
      "quantity": 1,
      "price": 450.00
    }
  ],
  "payment": {
    "id": 8,
    "type": "PIX",
    "amount": 450.00,
    "status": "PENDING",
    "pixKey": "carla@email.com",
    "pixHolderName": "Carla Souza"
  },
  "shippingAddress": {
    "id": 5,
    "street": "Rua das Flores",
    "number": "2",
    "complement": "Casa",
    "neighborhood": "Boa Viagem",
    "city": "Recife",
    "state": "PE",
    "zipCode": "50000000"
  },
  "total": 450.00
}
```

## Possíveis próximos passos

- Autenticação e autorização com Spring Security + JWT
- Testes automatizados (unitários com JUnit/Mockito, integração com `@SpringBootTest`)
- Interface web consumindo a API
