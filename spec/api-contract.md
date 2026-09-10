# API Contract Specification

**Base path:** `/api`  
**Content-Type:** `application/json`  
**Version:** 1.0

---

## Endpoints Summary

| Method | Path | Description | Success | Error |
|--------|------|-------------|---------|-------|
| POST | `/tickets` | Create ticket | 201 | 400 |
| GET | `/tickets` | List/search/filter | 200 | — |
| GET | `/tickets/{id}` | Get ticket + comments | 200 | 404 |
| PATCH | `/tickets/{id}` | Partial update | 200 | 400, 404, 409 |
| POST | `/tickets/{id}/comments` | Add comment | 201 | 400, 404 |

---

## POST /api/tickets

**Request**
```json
{
  "title": "Login broken",
  "description": "Users cannot log in with SSO",
  "priority": "HIGH",
  "assignee": "alice@example.com"
}
```

**Response 201**
```json
{
  "id": 1,
  "title": "Login broken",
  "description": "Users cannot log in with SSO",
  "status": "OPEN",
  "priority": "HIGH",
  "assignee": "alice@example.com",
  "createdAt": "2026-09-10T10:00:00Z",
  "updatedAt": "2026-09-10T10:00:00Z",
  "comments": []
}
```

---

## GET /api/tickets

**Query parameters**

| Param | Type | Required | Description |
|-------|------|----------|-------------|
| keyword | string | No | Case-insensitive match in title OR description |
| status | TicketStatus | No | Exact status filter |

**Combined behaviour:** When both present, apply AND (must match keyword AND status).

**Response 200**
```json
[
  {
    "id": 1,
    "title": "Login broken",
    "status": "OPEN",
    "priority": "HIGH",
    "assignee": "alice@example.com",
    "createdAt": "2026-09-10T10:00:00Z",
    "updatedAt": "2026-09-10T10:00:00Z",
    "comments": []
  }
]
```

*List response may omit `description` for brevity (optional optimisation; full object acceptable).*

---

## GET /api/tickets/{id}

**Response 200** — Full `TicketResponse` with comments array.

**Response 404**
```json
{
  "message": "Ticket not found: 99",
  "fieldErrors": {}
}
```

---

## PATCH /api/tickets/{id}

Partial update. All fields optional; only provided fields are changed.

**Request**
```json
{
  "title": "Updated title",
  "description": "Updated description",
  "priority": "URGENT",
  "assignee": "bob@example.com",
  "status": "IN_PROGRESS"
}
```

**Response 200** — Updated `TicketResponse`.

**Response 409** (invalid status transition)
```json
{
  "message": "Invalid status transition from CLOSED to OPEN",
  "fieldErrors": {}
}
```

**Response 400** (validation)
```json
{
  "message": "Validation failed",
  "fieldErrors": {
    "title": "must not be blank"
  }
}
```

---

## POST /api/tickets/{id}/comments

**Request**
```json
{
  "author": "alice@example.com",
  "body": "Investigating SSO configuration"
}
```

**Response 201** — Full `TicketResponse` including new comment.

---

## Standard Error Envelope

All error responses use:
```json
{
  "message": "string",
  "fieldErrors": {
    "fieldName": "error detail"
  }
}
```

| HTTP | When |
|------|------|
| 400 | Validation failure, malformed request |
| 404 | Ticket ID not found |
| 409 | Invalid status transition (business rule) |
| 500 | Unexpected server error |

---

## Enum Values (JSON strings)

- **Status:** `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED`, `CANCELLED`
- **Priority:** `LOW`, `MEDIUM`, `HIGH`, `URGENT`
