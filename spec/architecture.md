# Architecture Specification

**Version:** 1.0  
**Status:** Approved for implementation planning

---

## 1. High-Level Architecture

```
┌──────────────────┐         REST/JSON          ┌─────────────────────────────┐
│   Next.js UI     │  ◄──────────────────────►  │   Spring Boot REST API      │
│   (port 3000)    │                            │   (port 8080)               │
└──────────────────┘                            └──────────────┬──────────────┘
                                                               │
                                                               ▼
                                               ┌───────────────────────────────┐
                                               │  Controller                   │
                                               │  (HTTP, validation, DTO map)  │
                                               └──────────────┬────────────────┘
                                                              │
                                                              ▼
                                               ┌───────────────────────────────┐
                                               │  Service / Business Logic     │
                                               │  (state machine, transactions)│
                                               └──────────────┬────────────────┘
                                                              │
                                                              ▼
                                               ┌───────────────────────────────┐
                                               │  Repository (Spring Data JPA) │
                                               └──────────────┬────────────────┘
                                                              │
                                                              ▼
                                               ┌───────────────────────────────┐
                                               │  PostgreSQL (runtime)         │
                                               │  H2 (tests only)              │
                                               └───────────────────────────────┘
```

---

## 2. Layer Responsibilities

### Frontend (Next.js)
- Render UI per `spec/ui-flow.md`
- Call REST API via typed client (`lib/api.ts`)
- Parse and display API errors (`message`, `fieldErrors`)
- **Must NOT** be the sole enforcer of status transitions

### Controller
- Map HTTP requests to service calls
- Apply `@Valid` on request DTOs
- Return DTOs with correct HTTP status codes
- **No business logic** (no state machine checks here)

### Service
- Orchestrate use cases: create, update, search, comment
- Enforce state machine via `TicketStatusMachine`
- Manage transactions (`@Transactional`)
- Throw domain exceptions (`InvalidStatusTransitionException`, `ResourceNotFoundException`)

### Repository
- Data access via Spring Data JPA
- Custom query for keyword search + status filter
- **No business rules**

### Domain (Entities)
- `Ticket`, `Comment` JPA entities
- Enums: `TicketStatus`, `TicketPriority`
- Mapping annotations only; no HTTP concerns

---

## 3. State Machine in Architecture

```
TicketController.patch()
        │
        ▼
TicketService.updateTicket()
        │
        ├── if status unchanged → skip transition check
        │
        └── TicketStatusMachine.canTransition(from, to)
                 │
                 ├── true  → persist new status
                 └── false → throw InvalidStatusTransitionException → 409
```

- `TicketStatusMachine` is a stateless `@Component`
- Unit-tested independently of Spring context
- Transition table defined in `spec/state-machine.md`

---

## 4. Validation & Error Handling

| Layer | Responsibility |
|-------|----------------|
| DTO | Jakarta Validation annotations (`@NotBlank`, `@Size`, `@NotNull`) |
| Controller | `@Valid` triggers 400 via `GlobalExceptionHandler` |
| Service | Business rules (state machine) → 409 |
| Service | Not found → 404 |

**Standard error body:**
```json
{
  "message": "Human-readable summary",
  "fieldErrors": { "fieldName": "error detail" }
}
```

---

## 5. Configuration & Profiles

| Profile | Database | Purpose |
|---------|----------|---------|
| `dev` / default | PostgreSQL | Local development, data persists |
| `test` | H2 in-memory | Automated tests, isolated |
| `prod` | PostgreSQL (env vars) | Deployment |

Environment variables (prod): `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `CORS_ORIGINS`.

---

## 6. Testing Architecture

```
┌─────────────────────────┐     ┌──────────────────────────────┐
│ TicketStatusMachineTest │     │ TicketApiIntegrationTest     │
│ (pure unit, no Spring)  │     │ (@SpringBootTest + MockMvc)  │
└─────────────────────────┘     │ Profile: test, DB: H2        │
                                └──────────────────────────────┘
┌─────────────────────────┐
│ Manual UI checklist     │
│ (frontend + PostgreSQL) │
└─────────────────────────┘
```

---

## 7. Repository Layout (Planned)

```
backend/
  src/main/java/com/support/tickets/
    SupportTicketApplication.java
    config/          # CORS, etc.
    controller/      # REST endpoints
    dto/             # Request/response records
    domain/          # JPA entities, enums
    repository/      # Spring Data interfaces
    service/         # Business logic, state machine
    exception/       # Custom exceptions + handler
  src/main/resources/
    application.yml
    application-dev.yml
    application-test.yml
    application-prod.yml
  src/test/java/...

frontend/
  src/app/           # Next.js pages
  src/lib/           # API client, types
```

---

## 8. Cross-Cutting Concerns

- **CORS:** Allow frontend origin in dev (`http://localhost:3000`)
- **Logging:** SLF4J defaults; no sensitive data in logs
- **Secrets:** Never committed; `.gitignore` covers `.env`, credentials
- **AI steering:** `rules/`, `commands/`, `skills/` guide all AI-generated code
