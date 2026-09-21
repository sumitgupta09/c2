# Data Model Specification

**Version:** 1.1 (aligned with auth + ticket types)

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
│ ticket_type         │                     └─────────────────────┘
│ assignee            │
│ created_by          │
│ created_at          │
│ updated_at          │
└─────────────────────┘

┌─────────────────────┐
│        User         │  (staff accounts)
├─────────────────────┤
│ id (PK)             │
│ email (unique)      │
│ name                │
│ password_hash       │
│ role                │
│ team                │
└─────────────────────┘
```

---

## Ticket

| Column | Java Type | DB Type | Constraints | Notes |
|--------|-----------|---------|-------------|-------|
| id | Long | BIGSERIAL / IDENTITY | PK, auto | |
| title | String | VARCHAR(200) | NOT NULL | |
| description | String | VARCHAR(5000) | NOT NULL | |
| status | TicketStatus | VARCHAR(20) | NOT NULL, default OPEN | Enum as string |
| priority | TicketPriority | VARCHAR(20) | NOT NULL | Auto from ticket type on create |
| ticket_type | TicketType | VARCHAR(20) | NOT NULL | Drives auto-assign + priority |
| assignee | String | VARCHAR(100) | NOT NULL | Staff email; auto-assigned |
| created_by | String | VARCHAR(100) | NOT NULL | Reporter / guest identifier |
| created_at | Instant | TIMESTAMP | NOT NULL | Set on insert |
| updated_at | Instant | TIMESTAMP | NOT NULL | Set on insert/update |

**Table name:** `tickets`

---

## Comment

| Column | Java Type | DB Type | Constraints | Notes |
|--------|-----------|---------|-------------|-------|
| id | Long | BIGSERIAL / IDENTITY | PK, auto | |
| ticket_id | Long | BIGINT | FK → tickets.id, NOT NULL | |
| author | String | VARCHAR(100) | NOT NULL | |
| body | String | VARCHAR(5000) | NOT NULL | |
| created_at | Instant | TIMESTAMP | NOT NULL | Immutable after insert |

**Table name:** `comments`  
**Cascade:** Deleting a ticket deletes its comments (`orphanRemoval = true`).

---

## User (staff)

| Column | Java Type | Constraints | Notes |
|--------|-----------|-------------|-------|
| id | Long | PK | |
| email | String | unique, NOT NULL | Login id |
| name | String | NOT NULL | |
| password_hash | String | NOT NULL | BCrypt |
| role | UserRole | NOT NULL | ADMIN, TEAM_ADMIN, AGENT |
| team | SupportTeam | NOT NULL | Routing / authz |

---

## Enums

### TicketStatus
`OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `CANCELLED`

### TicketPriority
`LOW`, `MEDIUM`, `HIGH`, `URGENT`

### TicketType
`TECHNICAL`, `DATABASE`, `HR`, `BILLING`, `ACCOUNT`, `OTHER`

### UserRole
`ADMIN`, `TEAM_ADMIN`, `AGENT`

### SupportTeam
`SUPPORT_DESK`, `IT`, `DATABASE`, `HR`, `FINANCE`, `ACCOUNTS`

---

## Indexes (Recommended)

| Index | Columns | Purpose |
|-------|---------|---------|
| `idx_tickets_status` | status | Status filter |
| `idx_tickets_updated_at` | updated_at DESC | List sort order |

Search via JPQL `LIKE` on title/description (assessment scope).

---

## DTO Mapping (API Boundary)

| Entity Field | Response DTO | Notes |
|--------------|----------------|-------|
| All ticket fields | `TicketResponse` | Includes nested `comments` |
| Comment fields | `CommentResponse` | No `ticketId` in response |
| User (safe fields) | `UserResponse` / `AuthResponse` | Never return password hash |

Entities are **never** returned directly from REST controllers.
