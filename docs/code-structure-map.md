# Code Structure Map (Graphify equivalent)

In-repo substitute for external **Graphify**: a stable map of layers and dependencies for token-efficient prompting.

```
frontend (Next.js :3000)
  app/* pages  →  lib/api.ts  →  REST /api/*
        │
        ▼
backend (Spring Boot :8080)
  controller/  →  service/  →  repository/  →  JPA entities
                      │
                      ├─ TicketStatusMachine   (state transitions)
                      ├─ TicketAssignmentService (type → assignee/priority)
                      └─ TicketAuthorizationService (roles/teams)
        │
        ▼
  H2 file (dev)  |  H2 mem (test)  |  PostgreSQL (prod :5433)
```

## Package map (`backend/.../com/support/tickets`)

| Package | Responsibility | Depend on |
|---------|----------------|-----------|
| `controller` | HTTP, DTO in/out | `service`, `dto` |
| `service` | Business rules, SM, authz | `repository`, `domain` |
| `repository` | Persistence queries | `domain` |
| `domain` | Entities, enums | — |
| `dto` | API boundary records | enums only |
| `security` | JWT filter/service | — |
| `config` | CORS, security, seeder | — |
| `exception` | 400/404/409 mapping | `dto.ErrorResponse` |

## Prompting tip
Before adding an endpoint, attach this file + one spec — not the whole `backend/` tree.
