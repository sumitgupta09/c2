# Gupta Corp Support Desk

Support Ticket Management System — full-stack implementation with spec-driven development.

## Quick Start

```bash
# Backend (Java 21)
export JAVA_HOME=~/.sdkman/candidates/java/21.0.1-amzn
cd backend && ./run.sh run

# Frontend (separate terminal)
cd frontend && npm run dev
```

- **Employee portal:** http://localhost:3000
- **API:** http://localhost:8080/api
- **Smoke test (after servers are up):** `./scripts/smoke-all.sh` or `cd backend && ./run.sh smoke`

Copy `.env.example` to `.env` for custom JWT secret (optional for local dev).

---

## Core Features (Implemented)

| Feature | UI | API |
|---------|----|-----|
| Create ticket | `/tickets/new` | `POST /api/tickets` |
| List tickets | `/dashboard` (staff) | `GET /api/tickets` |
| View ticket details | `/tickets/[id]` | `GET /api/tickets/{id}` |
| Update title, description, priority, assignee | Ticket detail form | `PATCH /api/tickets/{id}` |
| Add comments | Ticket detail | `POST /api/tickets/{id}/comments` |
| Search by keyword | Dashboard search box | `GET /api/tickets?keyword=` |
| Filter by status | Dashboard status dropdown | `GET /api/tickets?status=` |
| Backend validation | Error messages in UI | `@Valid` DTOs → 400 + `fieldErrors` |
| Database persistence | H2 file DB (dev) | `backend/data/` survives restart |

---

## State Machine (Backend-Enforced)

```
OPEN → IN_PROGRESS → RESOLVED → CLOSED
OPEN → CANCELLED
IN_PROGRESS → CANCELLED
```

Invalid transitions return **409 Conflict** (e.g. `CLOSED → OPEN` ❌).

Unit tests: `TicketStatusMachineTest`  
Integration tests: `TicketApiIntegrationTest`

---

## Acceptance Criteria

| # | Criterion | Status |
|---|-----------|--------|
| 1 | Ticket can be created from UI | ✅ |
| 2 | Tickets can be listed | ✅ |
| 3 | Ticket details can be viewed | ✅ |
| 4 | Ticket fields can be updated | ✅ |
| 5 | Assignee can be changed | ✅ (admin / team lead) |
| 6 | Comments can be added | ✅ |
| 7 | Search works | ✅ |
| 8 | Status filter works | ✅ |
| 9 | Valid status transitions work | ✅ |
| 10 | Invalid transitions rejected by backend | ✅ 409 |
| 11 | Data survives application restart | ✅ H2 file DB |
| 12 | Backend validation works | ✅ |
| 13 | UI shows meaningful errors | ✅ |
| 14 | State-machine integration tests pass | ✅ `mvn test` |
| 15 | No secrets committed | ✅ `.env` gitignored; use `.env.example` |

Run tests:

```bash
cd backend && mvn test
```

---

## Demo Staff Accounts

| Role | Email | Password |
|------|-------|----------|
| Support Desk Manager | `support.admin@guptacorp.com` | `admin123` |
| IT Team Lead | `it.lead@guptacorp.com` | `lead123` |
| IT Agent | `it.agent@guptacorp.com` | `agent123` |
| HR Team Lead | `hr.lead@guptacorp.com` | `lead123` |

Public users can raise requests and track by ticket ID without login.

---

## Spec-Kit Workflow

| Step | Artifact |
|------|----------|
| Constitution | `.constitution.md` + `rules/` |
| Specify | `spec/requirements.md`, `docs/requirements-analysis.md` |
| Plan | `spec/architecture.md`, `spec/api-contract.md`, … |
| Tasks | `docs/implementation-plan.md` |
| Implement | `backend/`, `frontend/` |
| Review | `commands/review-code.md`, `commands/self-critique.md` |
| Traceability | `docs/requirements-traceability.md` |

Prompt templates: `prompts/templates.md`, `prompts/few-shot-examples.md`

---

## PostgreSQL (prod profile)

```bash
docker compose up -d
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:postgresql://localhost:5433/tickets
export DATABASE_USERNAME=tickets
export DATABASE_PASSWORD=tickets
export JWT_SECRET=your-32-char-min-secret-here
export SPRING_JPA_HIBERNATE_DDL_AUTO=update
cd backend && mvn spring-boot:run
```

Uses `application-prod.yml` (PostgreSQL via Docker on port **5433** — avoids conflict with local Postgres on 5432).

Verify restart persistence against PostgreSQL:

```bash
./scripts/verify-postgres.sh
```

---

## MCP Server (Cursor)

```bash
cd mcp-server && npm install
```

Backend must be running. Cursor loads `.cursor/mcp.json` — tools: `get_ticket`, `list_ticket_types`, `create_ticket`.

See `docs/mcp-debugging.md` for troubleshooting.

---

## Repository Structure

```
├── .constitution.md  # Project constitution (Spec-Kit)
├── spec/             # Specifications (source of truth)
├── docs/             # Analysis, traceability, planning
├── rules/            # AI steering rules
├── commands/         # Review & test generation workflows
├── prompts/          # Reusable prompt templates
├── mcp-server/       # MCP tools for ticket API
├── backend/          # Java 21 / Spring Boot API
└── frontend/         # Next.js UI
```

See `docs/requirements-traceability.md` for full requirement → implementation mapping.
