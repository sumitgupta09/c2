# Java Spring Boot Guidelines

## Stack
- **Java 21** (records, pattern matching, modern APIs)
- **Spring Boot 3.x** (Spring Framework 6, Jakarta EE namespace)
- **Spring Web** — REST controllers
- **Spring Data JPA** — persistence
- **Jakarta Bean Validation** — input validation
- **PostgreSQL** — runtime database
- **H2** — automated tests only

---

## Package Structure

```
com.support.tickets
├── SupportTicketApplication.java
├── config/           # WebConfig (CORS), other @Configuration
├── controller/       # @RestController — HTTP only
├── dto/              # Request/response records
├── domain/           # @Entity, enums
├── repository/       # Spring Data JpaRepository interfaces
├── service/          # Business logic, @Transactional
└── exception/        # Custom exceptions + @ControllerAdvice
```

---

## Layer Responsibilities

| Layer | Does | Does NOT |
|-------|------|----------|
| Controller | HTTP mapping, `@Valid`, status codes | Business rules, state machine |
| Service | Use cases, transactions, state machine | HTTP concerns, SQL |
| Repository | Data access | Business validation |
| Domain | Entity mapping | API exposure |
| DTO | API contract | JPA annotations |

---

## Dependency Injection
- Constructor injection only (no `@Autowired` on fields)
- Services depend on repositories and `TicketStatusMachine`
- Controllers depend on services only
- Keep beans stateless where possible

---

## DTO / Entity Separation
- **Always** use records for request/response DTOs
- **Never** return `@Entity` from controllers
- Map entity → DTO in service or dedicated mapper (inline mapping acceptable for small project)
- DTOs live in `dto/` package; entities in `domain/`

---

## JPA Conventions
- `@Entity` classes use explicit table/column names where clarity needed
- Enums stored as `@Enumerated(EnumType.STRING)`
- `@PrePersist` / `@PreUpdate` for timestamps
- Lazy collections (`@OneToMany`) — never serialize entities directly
- `ddl-auto: update` for dev; `validate` for prod

---

## Transactions
- `@Transactional` on service methods that write data
- `@Transactional(readOnly = true)` on read-only service methods
- Do not open transactions in controllers

---

## Validation
- Jakarta annotations on DTOs: `@NotBlank`, `@NotNull`, `@Size`
- Trigger with `@Valid @RequestBody` in controller
- Business validation (state machine) in service → throws domain exception

---

## Exception Handling
- `@RestControllerAdvice` with `@ExceptionHandler` methods
- Consistent `ErrorResponse` record: `{ message, fieldErrors }`
- Map exceptions to HTTP status:
  - `MethodArgumentNotValidException` → 400
  - `ResourceNotFoundException` → 404
  - `InvalidStatusTransitionException` → 409

---

## Configuration
- `application.yml` — shared settings
- `application-dev.yml` — PostgreSQL local, CORS
- `application-test.yml` — H2 in-memory
- `application-prod.yml` — env var placeholders
- **No secrets in YAML files** — use `${ENV_VAR}` with empty default

---

## Maintainability
- One class per responsibility
- State machine in dedicated `TicketStatusMachine` component
- Custom queries in repository interface (`@Query`)
- Avoid Lombok unless team agrees — plain getters/setters or records

---

## Avoid Unnecessary Complexity
- No CQRS, event sourcing, or microservices
- No generic `BaseEntity` abstraction unless reused ≥ 3 times
- No separate API versioning
- No MapStruct unless mapping becomes unwieldy
