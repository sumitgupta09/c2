# Requirements Analysis

**Project:** Support Ticket Management System  
**Phase:** Requirement Analysis (pre-implementation)  
**Date:** 2026-09-10

This document translates the assignment brief into engineering-ready, testable requirements.

---

## A. Functional Requirements

| ID | Requirement | Testable Acceptance |
|----|-------------|---------------------|
| FR-01 | Create a ticket with title, description, priority, and optional assignee | `POST /api/tickets` returns 201; ticket persisted with status `OPEN` |
| FR-02 | List all tickets | `GET /api/tickets` returns 200 and array of tickets |
| FR-03 | View ticket details including comments | `GET /api/tickets/{id}` returns 200 with full ticket + comments |
| FR-04 | Update ticket title | `PATCH /api/tickets/{id}` with `title` updates persisted value |
| FR-05 | Update ticket description | `PATCH /api/tickets/{id}` with `description` updates persisted value |
| FR-06 | Update ticket priority | `PATCH /api/tickets/{id}` with `priority` updates persisted value |
| FR-07 | Update ticket assignee | `PATCH /api/tickets/{id}` with `assignee` updates persisted value |
| FR-08 | Add comments to a ticket | `POST /api/tickets/{id}/comments` returns 201; comment appears on subsequent GET |
| FR-09 | Search tickets by keyword | `GET /api/tickets?keyword=` returns only tickets matching title or description (case-insensitive) |
| FR-10 | Filter tickets by status | `GET /api/tickets?status=` returns only tickets with matching status |
| FR-11 | Persist ticket data in database | Data retrievable after application restart |
| FR-12 | Validate input at backend | Invalid payloads return 400 with field-level errors |
| FR-13 | Display meaningful errors in UI | UI surfaces API `message` and `fieldErrors` to the user |

---

## B. Non-Functional Requirements

| ID | Requirement | Testable Acceptance |
|----|-------------|---------------------|
| NFR-01 | Backend uses Java 21 and Spring Boot | `pom.xml` / build targets Java 21; app starts with Spring Boot |
| NFR-02 | Runtime persistence uses PostgreSQL | App connects to PostgreSQL in default/runtime profile; data survives restart |
| NFR-03 | Automated tests may use H2 | Test profile uses in-memory or isolated H2 without requiring PostgreSQL |
| NFR-04 | REST API over HTTP/JSON | All endpoints accept/produce JSON |
| NFR-05 | Frontend uses React/Next.js or equivalent | UI is a separate client consuming REST API |
| NFR-06 | Maintainable layered architecture | Controller → Service → Repository separation enforced |
| NFR-07 | No secrets in repository | `.gitignore` excludes credentials; config via env vars |
| NFR-08 | Spec-driven development artifacts present | `spec/`, `rules/`, `commands/`, `docs/` exist and are consistent |

---

## C. Business Rules

| ID | Rule | Testable Acceptance |
|----|------|---------------------|
| BR-01 | New tickets start in `OPEN` status | Created ticket always has `status: OPEN` |
| BR-02 | Status transitions follow defined state machine | Only allowed transitions succeed |
| BR-03 | Invalid transitions are rejected by backend | Direct API call with invalid transition returns 409 |
| BR-04 | Frontend is not authoritative for status rules | Malicious client bypassing UI still gets 409 from API |
| BR-05 | Comments are append-only | No API to edit/delete comments (unless future scope added) |
| BR-06 | Assignee is optional | Ticket can be created/updated with null or empty assignee |
| BR-07 | Search matches title OR description | Keyword present in either field includes ticket in results |
| BR-08 | Status filter is exact match | `?status=OPEN` excludes `IN_PROGRESS` tickets |

---

## D. Validation Rules

| ID | Field / Context | Rule | Error |
|----|-----------------|------|-------|
| VR-01 | `title` on create | Required, max 200 chars | 400, field error on `title` |
| VR-02 | `description` on create | Required, max 5000 chars | 400, field error on `description` |
| VR-03 | `priority` on create | Required, enum value | 400, field error on `priority` |
| VR-04 | `assignee` | Optional, max 100 chars | 400 if exceeds max length |
| VR-05 | `author` on comment | Required, max 100 chars | 400, field error on `author` |
| VR-06 | `body` on comment | Required, max 5000 chars | 400, field error on `body` |
| VR-07 | `status` on update | Must be valid enum | 400 if unknown value |
| VR-08 | `status` on update | Must be valid transition | 409 if business rule violated |
| VR-09 | Ticket ID | Must exist | 404 if ticket not found |

---

## E. State-Machine Rules

| ID | From | To | Valid? |
|----|------|-----|--------|
| SM-01 | OPEN | IN_PROGRESS | Yes |
| SM-02 | OPEN | CANCELLED | Yes |
| SM-03 | IN_PROGRESS | RESOLVED | Yes |
| SM-04 | IN_PROGRESS | CANCELLED | Yes |
| SM-05 | RESOLVED | CLOSED | Yes |
| SM-06 | CLOSED | OPEN | No → 409 |
| SM-07 | RESOLVED | OPEN | No → 409 |
| SM-08 | CANCELLED | OPEN | No → 409 |
| SM-09 | OPEN | RESOLVED | No → 409 |
| SM-10 | OPEN | CLOSED | No → 409 |
| SM-11 | Same status | Same status | Yes (no-op transition) |

**Enforcement location:** Service layer (`TicketStatusMachine` + `TicketService`), not controller or frontend.

**HTTP response for invalid transition:** `409 Conflict` with body:
```json
{ "message": "Invalid status transition from CLOSED to OPEN", "fieldErrors": {} }
```

---

## F. Persistence Requirements

| ID | Requirement | Testable Acceptance |
|----|-------------|---------------------|
| PR-01 | Tickets stored in PostgreSQL at runtime | Restart app; previously created tickets still retrievable |
| PR-02 | Comments linked to tickets via FK | Deleting ticket cascades or prevents orphan comments per data model |
| PR-03 | Timestamps auto-managed | `createdAt` immutable; `updatedAt` changes on ticket update |
| PR-04 | H2 used only for automated tests | `application-test.yml` uses H2; production/dev runtime uses PostgreSQL |
| PR-05 | Schema managed via JPA ddl-auto or migrations | Tables created/validated on startup per profile |

---

## G. REST API Requirements

| ID | Requirement | Testable Acceptance |
|----|-------------|---------------------|
| API-01 | Base path `/api` | All endpoints prefixed with `/api` |
| API-02 | `POST /api/tickets` creates ticket | 201 + body |
| API-03 | `GET /api/tickets` lists with optional filters | 200 + array |
| API-04 | `GET /api/tickets/{id}` returns detail | 200 or 404 |
| API-05 | `PATCH /api/tickets/{id}` partial update | 200 or 400/404/409 |
| API-06 | `POST /api/tickets/{id}/comments` adds comment | 201 or 400/404 |
| API-07 | Consistent error JSON format | All errors return `{ message, fieldErrors }` |
| API-08 | CORS configured for frontend origin | Browser client on localhost:3000 can call API |

---

## H. Frontend Requirements

| ID | Requirement | Testable Acceptance |
|----|-------------|---------------------|
| FE-01 | Ticket list page | Displays tickets from API |
| FE-02 | Create ticket form | Submits to API; redirects or shows success |
| FE-03 | Ticket detail page | Shows all fields and comments |
| FE-04 | Edit ticket fields | PATCH on save |
| FE-05 | Status dropdown shows only valid next states | UX aid only; backend still enforces |
| FE-06 | Search input | Debounced call to `?keyword=` |
| FE-07 | Status filter dropdown | Calls `?status=` |
| FE-08 | Error display | Shows `message` banner and per-field errors |
| FE-09 | Add comment form | POST comment; list refreshes |

---

## I. Error-Handling Requirements

| ID | Scenario | HTTP | UI Behaviour |
|----|----------|------|--------------|
| EH-01 | Validation failure | 400 | Show field errors |
| EH-02 | Ticket not found | 404 | Show not-found message |
| EH-03 | Invalid status transition | 409 | Show conflict message (do not update local state) |
| EH-04 | Network/server error | 5xx | Show generic failure with retry guidance |
| EH-05 | Malformed JSON | 400 | Show parse/validation message |

---

## J. Testing Requirements

| ID | Requirement | Testable Acceptance |
|----|-------------|---------------------|
| TR-01 | State machine unit tests | All valid/invalid transitions covered |
| TR-02 | API integration tests | CRUD, search, filter, comments, 409, 400 |
| TR-03 | Persistence integration test | Data survives restart (PostgreSQL or test equivalent) |
| TR-04 | Tests isolated | Each test uses clean state or transactions |
| TR-05 | Manual UI checklist | Documented in `spec/test-strategy.md` |

---

## K. Security Requirements

| ID | Requirement | Testable Acceptance |
|----|-------------|---------------------|
| SEC-01 | No credentials in git | `git grep` finds no passwords/keys in tracked files |
| SEC-02 | DB credentials via env vars | `DATABASE_URL`, `DATABASE_PASSWORD` etc. in prod profile |
| SEC-03 | Backend enforces business rules | Invalid API calls rejected regardless of client |
| SEC-04 | Input length limits | Oversized payloads rejected at validation layer |

*Note: Authentication/authorization is out of scope unless added later.*

---

## L. Repository / AI Engineering Requirements

| ID | Requirement | Testable Acceptance |
|----|-------------|---------------------|
| AI-01 | Spec files exist before implementation | `spec/*.md` complete and reviewed |
| AI-02 | AI steering rules exist | `rules/*.md` present |
| AI-03 | Reusable AI commands exist | `commands/*.md` present |
| AI-04 | Documentation skill exists | `skills/documentation/documentation.md` present |
| AI-05 | Prompt history maintained | `docs/prompt-history.md` + `.specstory/history/` |
| AI-06 | AI mistakes documented during review | `docs/ai-review-notes.md` updated in review phase |
| AI-07 | Token optimisation strategy documented | `docs/ai-context-strategy.md` present |

---

## M. Acceptance Criteria (Assignment)

| # | Criterion | Trace ID |
|---|-----------|----------|
| 1 | Ticket can be created from UI | FR-01, FE-02 |
| 2 | Tickets can be listed | FR-02, FE-01 |
| 3 | Ticket details can be viewed | FR-03, FE-03 |
| 4 | Ticket fields can be updated | FR-04–FR-07, FE-04 |
| 5 | Assignee can be changed | FR-07 |
| 6 | Comments can be added | FR-08, FE-09 |
| 7 | Search works | FR-09, FE-06 |
| 8 | Status filter works | FR-10, FE-07 |
| 9 | Valid status transitions work | SM-01–SM-05 |
| 10 | Invalid status transitions rejected by backend | SM-06–SM-10, BR-03 |
| 11 | Data survives application restart | PR-01 |
| 12 | Backend validation works | VR-01–VR-09 |
| 13 | UI shows meaningful errors | FR-13, EH-01–EH-03 |
| 14 | State-machine integration tests pass | TR-01, TR-02 |
| 15 | No secrets are committed | SEC-01 |

---

## N. Ambiguities

| ID | Ambiguity | Resolution (documented assumption) |
|----|-----------|-----------------------------------|
| AMB-01 | Authentication not specified | Assume open API for assessment; no login required |
| AMB-02 | Assignee format not specified | Assume free-text string (e.g. email), max 100 chars |
| AMB-03 | Search: partial vs exact match | Assume case-insensitive substring match on title and description |
| AMB-04 | Combined search + filter | Assume AND semantics: both `keyword` and `status` applied when present |
| AMB-05 | Comment edit/delete | Assume not required; comments are append-only |
| AMB-06 | Pagination not specified | Assume unpaginated list acceptable for assessment scope |
| AMB-07 | Sort order not specified | Assume `updatedAt DESC` (most recently updated first) |
| AMB-08 | Who can transition status | Assume any API client; no role-based restrictions |

---

## O. Assumptions

1. Single-tenant system; no multi-org support required.
2. PostgreSQL available locally via Docker or native install for runtime.
3. H2 reserved for CI/unit/integration tests only.
4. Next.js 14 App Router chosen as frontend framework.
5. `PATCH` used for partial ticket updates (REST best practice).
6. Status change is part of ticket update (`PATCH`), not a separate endpoint.
7. Invalid transition returns **409 Conflict** (not 400 or 422).
8. No soft-delete for tickets in initial scope.
9. CORS limited to `http://localhost:3000` in development.
10. SpecStory or manual logging used for prompt history going forward.

---

## P. Risks

| ID | Risk | Impact | Mitigation |
|----|------|--------|------------|
| R-01 | AI generates state machine in controller | High — violates architecture | Enforce via `rules/java-springboot.md`; review in `commands/review-code.md` |
| R-02 | Entity exposed via REST | Medium — coupling, lazy-load issues | DTO-only responses in spec and rules |
| R-03 | Frontend-only status enforcement | High — security/business rule bypass | Integration tests for direct API invalid transitions |
| R-04 | Spec/code drift during AI generation | Medium | `commands/review-spec.md` after each feature |
| R-05 | Secrets committed by AI | High | `.gitignore`, env-based config, review checklist |
| R-06 | Over-engineering (auth, events, CQRS) | Medium — scope creep | Spec explicitly excludes unnecessary features |
| R-07 | Token waste loading full codebase | Low — cost/slow AI | `docs/ai-context-strategy.md` |
| R-08 | PostgreSQL not available locally | Medium — blocks dev | Document Docker Compose setup in implementation plan |
