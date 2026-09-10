# API Standards

## REST Conventions

| Principle | Rule |
|-----------|------|
| Base path | `/api` |
| Resource naming | Plural nouns: `/tickets`, `/tickets/{id}/comments` |
| IDs | Path variable: `{id}` (Long) |
| Content type | `application/json` only |
| Dates | ISO-8601 UTC (`Instant` serialized) |

---

## HTTP Methods

| Method | Usage | Idempotent |
|--------|-------|------------|
| GET | Read (list, detail) | Yes |
| POST | Create (ticket, comment) | No |
| PATCH | Partial update | Yes |
| DELETE | Not used in initial scope | — |

---

## HTTP Status Codes

| Code | When |
|------|------|
| 200 OK | Successful GET, PATCH |
| 201 Created | Successful POST |
| 400 Bad Request | Validation failure, malformed JSON |
| 404 Not Found | Ticket ID does not exist |
| 409 Conflict | Invalid status transition (business rule) |
| 500 Internal Server Error | Unhandled exception |

---

## Request DTOs

| Endpoint | DTO | Validation |
|----------|-----|------------|
| POST /tickets | `CreateTicketRequest` | title, description, priority required |
| PATCH /tickets/{id} | `UpdateTicketRequest` | All fields optional |
| POST /tickets/{id}/comments | `CreateCommentRequest` | author, body required |

Use Java `record` types with Jakarta Validation annotations.

---

## Response DTOs

| DTO | Used For |
|-----|----------|
| `TicketResponse` | Single ticket (includes comments) |
| `CommentResponse` | Nested in ticket response |
| `ErrorResponse` | All error responses |
| `List<TicketResponse>` | GET /tickets |

Never return JPA entities.

---

## Validation Error Format (400)

```json
{
  "message": "Validation failed",
  "fieldErrors": {
    "title": "must not be blank",
    "priority": "must not be null"
  }
}
```

---

## Business Error Format (409)

```json
{
  "message": "Invalid status transition from CLOSED to OPEN",
  "fieldErrors": {}
}
```

---

## Not-Found Format (404)

```json
{
  "message": "Ticket not found: 42",
  "fieldErrors": {}
}
```

---

## Search / Filter Conventions

| Parameter | Type | Example | Semantics |
|-----------|------|---------|-----------|
| keyword | query string | `?keyword=login` | Case-insensitive substring in title OR description |
| status | enum string | `?status=OPEN` | Exact match |

Combined: `?keyword=login&status=OPEN` → AND logic.

---

## CORS
- Dev: allow `http://localhost:3000`
- Prod: configure via `CORS_ORIGINS` env var
- Methods: GET, POST, PATCH, OPTIONS

---

## Versioning
- No API versioning in initial scope (`/api/tickets`, not `/api/v1/tickets`)

---

## OpenAPI (Optional)
- May add `springdoc-openapi` later for auto-generated docs
- `spec/api-contract.md` is source of truth until then
