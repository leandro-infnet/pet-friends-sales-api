# 🐾 Pet Friends - Sales API

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Axon Framework](https://img.shields.io/badge/Axon_Framework-CQRS-007396?style=for-the-badge&logo=java&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker&logoColor=white)

> **API de Vendas Escalonável para o Ecossistema Pet Friends**
>
> Implementação de referência utilizando arquitetura orientada a eventos, DDD, CQRS e Event Sourcing.

---

## 📖 Visão Geral

O módulo **Sales API** é responsável pelo ciclo de vida dos pedidos de venda na rede Pet Friends. Diferente de arquiteturas CRUD tradicionais, este projeto utiliza **Event Sourcing** para garantir auditabilidade total e **CQRS** (Command Query Responsibility Segregation) para escalar operações de leitura e escrita independentemente.

---

## 📚 Documentação Completa
Para detalhes profundos sobre as 12 questões arquiteturais e decisões de design, consulte o PDF oficial:
👉 **[Baixar Guia de Arquitetura (PDF)](docs/PetFriends_CQRS_Guide.pdf)**

---

### Mapa de Contexto

O serviço se situa no contexto de suporte a vendas, integrando-se com Pagamentos e Notificações dentro do ecossistema Pet Friends.

```mermaid
%%{init: {
  "theme": "neutral",
  "themeVariables": {
    "background": "#ffffff",
    "primaryTextColor": "#1e1e1e",
    "lineColor": "#333333",
    "fontFamily": "Cascadia Code, sans-serif",
    "fontSize": "15px",
    "clusterBkg": "#f9fafc",
    "clusterBorder": "#d0d0d0",
    "edgeLabelBackground":"#ffffff"
  }
}}%%
graph TD
    classDef core fill:#b5d9d9,stroke:#333,stroke-width:2px,color:#111;
    classDef supporting fill:#e6f2ff,stroke:#666,stroke-width:1.2px,color:#111;
    classDef generic fill:#f9f9f9,stroke:#999,stroke-width:1px,stroke-dasharray: 5 5,color:#111;

    subgraph PetFriendsSystem [🐾 Ecossistema Pet Friends]
        Agendamento(Agendamento):::core
        Clientes(Clientes):::supporting
        Vendas(Vendas / Catálogo):::supporting
        Pagamentos(Pagamentos):::generic
        Notificacoes(Notificações):::generic
    end

    Clientes -->|U/D| Vendas
    Vendas -->|U/D| Pagamentos
    Vendas -->|Pub Eventos| Notificacoes

```

---

## 🏗️ Arquitetura

O projeto segue estritamente os princípios de **Domain-Driven Design (DDD)** e separa responsabilidades em:

1. **Core API**: Contrato puro (Comandos, Eventos, Queries).
2. **Command Side**: Agregados que protegem invariantes de negócio.
3. **Query Side**: Projeções otimizadas para leitura (Materialized Views).

#### Fluxo de Dados (CQRS)

```mermaid
%%{init: {
  "theme": "neutral",
  "themeVariables": {
    "background": "#ffffff",
    "primaryTextColor": "#1e1e1e",
    "lineColor": "#333333",
    "fontFamily": "Cascadia Code, Consolas, monospace",
    "fontSize": "15px",
    "edgeLabelBackground":"#f9f9f9",
    "clusterBkg": "#f5f7fb",
    "clusterBorder": "#cccccc"
  }
}}%%
flowchart LR
    user((🧍‍♂️ Cliente))

    subgraph Write [📦 Write Model]
        CC[Controller] --> CB[Command Bus]
        CB --> Agg[Agregado Pedido]
        Agg --> ES[(Event Store)]
    end

    subgraph Read [📗 Read Model]
        EH[Projector]
        DB[(H2 Database)]
        QC[Query Controller]
    end

    user -->|POST| CC
    ES -.->|Event| EH
    EH -->|Save| DB
    user -->|GET| QC
    QC -->|Select| DB

    classDef cmd fill:#ffdddd,stroke:#444,stroke-width:1.5px,color:#111;
    classDef qry fill:#ddffdd,stroke:#444,stroke-width:1.5px,color:#111;
    class CC,CB,Agg,ES cmd;
    class EH,DB,QC qry;
```

#### Sequência de Criação de Pedido

```mermaid
%%{init: {
  "theme": "neutral",
  "themeVariables": {
    "background": "#ffffff",
    "primaryColor": "#e8f4fa",
    "primaryTextColor": "#1e1e1e",
    "secondaryColor": "#d4eaf7",
    "secondaryTextColor": "#1a1a1a",
    "actorTextColor": "#000000",
    "actorBorder": "#666666",
    "actorBkg": "#f5f9fc",
    "lineColor": "#333333",
    "signalColor": "#222222",
    "sequenceNumberColor": "#ffffff",
    "labelBoxBkgColor": "#ffffff",
    "labelBoxBorderColor": "#999999",
    "noteBkgColor": "#f0f0f0",
    "noteTextColor": "#111111",
    "activationBorderColor": "#888888",
    "activationBkgColor": "#d6ebff",
    "fontFamily": "Cascadia Code, Consolas, monospace",
    "fontSize": "15px"
  },
  "themeCSS": ".mermaid { background-color: #ffffff; color: #1e1e1e; }"
}}%%
sequenceDiagram
    participant Client as Cliente REST
    participant API as CommandController
    participant Bus as CommandGateway
    participant Agg as PedidoAggregate
    participant Store as EventStore
    participant Proj as PedidoProjector

    Client->>API: POST /orders
    API->>Bus: send(CriarPedidoCommand)
    Bus->>Agg: Valida e Aplica Evento
    Agg->>Store: apply(PedidoCriadoEvent)
    Store-->>Agg: Atualiza Estado (Event Sourcing)
    Bus-->>Client: 201 Created (ID)

    par Async Projection
        Store-)Proj: handle(PedidoCriadoEvent)
        Proj->>Proj: Salva no Banco de Leitura
    end
```

---

## 🚀 Tecnologias

- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.4.x
- **Arquitetura**: Axon Framework (Event Sourcing & CQRS)
- **Banco de Dados**: H2 (In-Memory para desenvolvimento)
- **Infraestrutura**: Docker (Axon Server)
- **Docs**: SpringDoc OpenAPI (Swagger UI)

---

## ⚙️ Como Executar

**Pré-requisitos**

- JDK 21+
- Maven 3.8+
- Docker (para o Axon Server)

1. **Inicie a Infraestrutura (Axon Server)**

O Axon Server é necessário para orquestrar comandos e armazenar eventos.

```bash
docker run -d --name axonserver -p 8024:8024 -p 8124:8124 axoniq/axonserver
```

_Acesse o Dashboard em: http://localhost:8024_

2. **Execute a Aplicação**

```bash
mvn spring-boot:run
```

3. **Documentação da API (Swagger)**

Com a aplicação rodando, acesse a interface interativa: 👉 **http://localhost:8080/swagger-ui/index.html**

---

## 📂 Estrutura do Projeto

A organização de pacotes reflete a intenção arquitetural:

```plaintext
br.com.petfriends.sales_api
├── coreapi          # 📦 O CONTRATO (Compartilhável)
│   ├── base         # Abstrações (BaseCommand, BaseEvent)
│   ├── commands     # Intenções (CriarPedidoCommand)
│   └── events       # Fatos (PedidoCriadoEvent)
│
├── command          # ✍️ O MODELO DE ESCRITA
│   ├── aggregates   # Regras de Negócio (PedidoAggregate)
│   └── services     # Orquestração
│
├── query            # 👓 O MODELO DE LEITURA
│   └── pedido       # Projeções, Repositórios e Event Handlers
│
└── controllers      # 🌐 ADAPTADORES REST
```

---

## 🧪 Testando via Terminal

Criar um novo pedido:

```bash
curl -X POST http://localhost:8080/orders \
   -H "Content-Type: application/json" \
   -d '{"clienteId": "cliente-badrobot", "valorTotal": 199.90}'
```

Listar todos os pedidos:

```bash
curl -X GET http://localhost:8080/orders
```

---

## 🤝 Contribuição

1. Faça um fork do projeto.
2. Crie sua feature branch (`git checkout -b feat/nova-feature`).
3. Siga o padrão **Conventional Commits**.
4. Abra um Pull Request.

**Pet Friends Engineering Team** © 2025
