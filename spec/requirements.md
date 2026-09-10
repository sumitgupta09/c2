# Requirements Specification

**Version:** 1.0  
**Status:** Approved for implementation planning  
**Source:** Assignment brief + `docs/requirements-analysis.md`

---

## 1. Functional Requirements

### FR-01 Create Ticket
- **Description:** User can create a support ticket.
- **Input:** `title` (required), `description` (required), `priority` (required), `assignee` (optional).
- **Behaviour:** Ticket created with `status = OPEN`.
- **Acceptance:** `POST /api/tickets` → 201; ticket retrievable via GET.

### FR-02 List Tickets
- **Description:** User can view all tickets.
- **Acceptance:** `GET /api/tickets` → 200 with array; UI list page renders results.

### FR-03 View Ticket Details
- **Description:** User can view a single ticket with all fields and comments.
- **Acceptance:** `GET /api/tickets/{id}` → 200; UI detail page shows data.

### FR-04–FR-07 Update Ticket Fields
- **Description:** User can update title, description, priority, and assignee.
- **Acceptance:** `PATCH /api/tickets/{id}` with any subset of fields → 200; changes persisted.

### FR-08 Add Comments
- **Description:** User can append comments to a ticket.
- **Input:** `author` (required), `body` (required).
- **Acceptance:** `POST /api/tickets/{id}/comments` → 201; comment visible on GET.

### FR-09 Search by Keyword
- **Description:** User can search tickets by keyword in title or description.
- **Acceptance:** `GET /api/tickets?keyword=login` returns matching tickets only (case-insensitive substring).

### FR-10 Filter by Status
- **Description:** User can filter tickets by status.
- **Acceptance:** `GET /api/tickets?status=OPEN` returns only OPEN tickets.

### FR-11 Persist Data
- **Description:** Ticket data survives application restart.
- **Acceptance:** Create ticket → restart app → ticket still retrievable from PostgreSQL.

### FR-12 Backend Validation
- **Description:** Invalid input rejected at API boundary.
- **Acceptance:** Missing/invalid fields → 400 with `fieldErrors` map.

### FR-13 UI Error Display
- **Description:** Frontend shows meaningful errors from API.
- **Acceptance:** Validation (400) and conflict (409) messages visible to user.

---

## 2. Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR-01 | Java 21, Spring Boot 3.x |
| NFR-02 | PostgreSQL for runtime persistence |
| NFR-03 | H2 for automated tests only |
| NFR-04 | REST API, JSON payloads |
| NFR-05 | Next.js frontend (separate process) |
| NFR-06 | Layered architecture: Controller → Service → Repository |
| NFR-07 | No secrets in version control |
| NFR-08 | Spec-driven artifacts maintained in repo |

---

## 3. Validation Rules

| Field | Create | Update | Rule |
|-------|--------|--------|------|
| title | Required | Optional | Not blank, max 200 |
| description | Required | Optional | Not blank, max 5000 |
| priority | Required | Optional | Enum: LOW, MEDIUM, HIGH, URGENT |
| assignee | Optional | Optional | Max 100 chars |
| status | N/A | Optional | Valid enum + valid transition |
| author (comment) | Required | N/A | Not blank, max 100 |
| body (comment) | Required | N/A | Not blank, max 5000 |

---

## 4. Acceptance Criteria (Assignment)

| # | Criterion | Status |
|---|-----------|--------|
| 1 | Ticket can be created from UI | Planned |
| 2 | Tickets can be listed | Planned |
| 3 | Ticket details can be viewed | Planned |
| 4 | Ticket fields can be updated | Planned |
| 5 | Assignee can be changed | Planned |
| 6 | Comments can be added | Planned |
| 7 | Search works | Planned |
| 8 | Status filter works | Planned |
| 9 | Valid status transitions work | Planned |
| 10 | Invalid status transitions rejected by backend | Planned |
| 11 | Data survives application restart | Planned |
| 12 | Backend validation works | Planned |
| 13 | UI shows meaningful errors | Planned |
| 14 | State-machine integration tests pass | Planned |
| 15 | No secrets are committed | Planned |

---

## 5. Out of Scope

- Authentication and authorization
- Pagination
- Comment edit/delete
- Audit trail / notifications
- Multi-tenancy

---

## 6. Assumptions

See `docs/requirements-analysis.md` § O for full list. Key assumptions:
- Assignee is free-text (email-like), not a user FK.
- Search and status filter combine with AND logic.
- Invalid status transition returns **409 Conflict**.
