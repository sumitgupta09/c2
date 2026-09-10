# Data Model Specification

**Version:** 1.0

---

## Entity Relationship

```
┌─────────────────────┐         1:N         ┌─────────────────────┐
│       Ticket        │────────────────────►│       Comment       │
├─────────────────────┤                     ├─────────────────────┤
│ id (PK)             │                     │ id (PK)             │
│ title               │                     │ ticket_id (FK)      │
│ description         │                     │ author              │
│ status              │                     │ body                │
│ priority            │                     │ created_at          │
│ assignee (nullable) │                     └─────────────────────┘
│ created_at          │
│ updated_at          │
└─────────────────────┘
```

---

## Ticket

| Column | Java Type | DB Type | Constraints | Notes |
|--------|-----------|---------|-------------|-------|
| id | Long | BIGSERIAL | PK, auto | |
| title | String | VARCHAR(200) | NOT NULL | |
| description | String | VARCHAR(5000) | NOT NULL | |
| status | TicketStatus | VARCHAR(20) | NOT NULL, default OPEN | Enum as string |
| priority | TicketPriority | VARCHAR(20) | NOT NULL, default MEDIUM | Enum as string |
| assignee | String | VARCHAR(100) | NULLABLE | Free-text |
| created_at | Instant | TIMESTAMP | NOT NULL | Set on insert |
| updated_at | Instant | TIMESTAMP | NOT NULL | Set on insert/update |

**Table name:** `tickets`

---

## Comment

| Column | Java Type | DB Type | Constraints | Notes |
|--------|-----------|---------|-------------|-------|
| id | Long | BIGSERIAL | PK, auto | |
| ticket_id | Long | BIGINT | FK → tickets.id, NOT NULL | |
| author | String | VARCHAR(100) | NOT NULL | |
| body | String | VARCHAR(5000) | NOT NULL | |
| created_at | Instant | TIMESTAMP | NOT NULL | Immutable after insert |

**Table name:** `comments`  
**Cascade:** Deleting a ticket deletes its comments (`orphanRemoval = true`).

---

## Enums

### TicketStatus
`OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `CANCELLED`

### TicketPriority
`LOW`, `MEDIUM`, `HIGH`, `URGENT`

---

## Indexes (Recommended)

| Index | Columns | Purpose |
|-------|---------|---------|
| `idx_tickets_status` | status | Status filter |
| `idx_tickets_updated_at` | updated_at DESC | List sort order |

Full-text search via JPQL `LIKE` on title/description (sufficient for assessment scope).

---

## DTO Mapping (API Boundary)

| Entity Field | Response DTO | Notes |
|--------------|----------------|-------|
| All ticket fields | `TicketResponse` | Includes nested `comments` |
| Comment fields | `CommentResponse` | No `ticketId` in response (implicit from parent) |

Entities are **never** returned directly from REST controllers.
