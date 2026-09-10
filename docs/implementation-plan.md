# Implementation Plan

**Phase:** Plan / Tasks (pre-implementation)  
**Prerequisite:** Specifications and AI steering complete  
**Rule:** Implement one task at a time; update spec if requirements change.

---

## Phase 0 — Environment Setup
| # | Task | Output | Depends On |
|---|------|--------|------------|
| 0.1 | Initialize git, `.gitignore` | Clean repo, no secrets tracked | — |
| 0.2 | Docker Compose for PostgreSQL | `docker-compose.yml` | spec/architecture.md |
| 0.3 | Spring Boot project skeleton (Java 21) | `backend/pom.xml`, main class | rules/java-springboot.md |
| 0.4 | Next.js project skeleton | `frontend/` | spec/ui-flow.md |
| 0.5 | Configure profiles: PostgreSQL (runtime), H2 (test) | `application-*.yml` | spec/architecture.md |

---

## Phase 1 — Domain & State Machine
| # | Task | Output | Depends On |
|---|------|--------|------------|
| 1.1 | Create enums: `TicketStatus`, `TicketPriority` | `domain/` | spec/data-model.md |
| 1.2 | Implement `TicketStatusMachine` | `service/TicketStatusMachine.java` | spec/state-machine.md |
| 1.3 | Unit tests for all transitions | `TicketStatusMachineTest` | commands/generate-tests.md |

**AI prompt example:**
> Implement `TicketStatusMachine` per @spec/state-machine.md. Write parameterized unit tests per @commands/generate-tests.md. Do not create controller or entities yet.

---

## Phase 2 — Persistence Layer
| # | Task | Output | Depends On |
|---|------|--------|------------|
| 2.1 | JPA entities: `Ticket`, `Comment` | `domain/` | spec/data-model.md |
| 2.2 | `TicketRepository` with search query | `repository/` | spec/api-contract.md |
| 2.3 | Verify schema against PostgreSQL | Tables created | Phase 0 |

---

## Phase 3 — Service Layer
| # | Task | Output | Depends On |
|---|------|--------|------------|
| 3.1 | DTOs: request/response records | `dto/` | spec/api-contract.md |
| 3.2 | `TicketService`: create, get, list, update, comment | `service/` | Phases 1–2 |
| 3.3 | Enforce state machine in `updateTicket` | 409 on invalid | spec/state-machine.md |
| 3.4 | Custom exceptions + `@ControllerAdvice` | `exception/` | rules/api-standards.md |

---

## Phase 4 — REST API
| # | Task | Output | Depends On |
|---|------|--------|------------|
| 4.1 | `TicketController` endpoints | `controller/` | spec/api-contract.md |
| 4.2 | Jakarta Validation on DTOs | 400 responses | spec/requirements.md |
| 4.3 | CORS config for frontend | `config/WebConfig` | spec/architecture.md |
| 4.4 | API integration tests (MockMvc + H2) | `TicketApiIntegrationTest` | spec/test-strategy.md |

---

## Phase 5 — Frontend
| # | Task | Output | Depends On |
|---|------|--------|------------|
| 5.1 | API client with error parsing | `lib/api.ts` | spec/api-contract.md |
| 5.2 | Ticket list page (search + filter) | `app/page.tsx` | spec/ui-flow.md |
| 5.3 | Create ticket page | `app/tickets/new/page.tsx` | spec/ui-flow.md |
| 5.4 | Ticket detail page (edit + comments) | `app/tickets/[id]/page.tsx` | spec/ui-flow.md |
| 5.5 | Status dropdown (UX only) | Detail page | spec/state-machine.md |

---

## Phase 6 — Testing & Review
| # | Task | Output | Depends On |
|---|------|--------|------------|
| 6.1 | Run full backend test suite | All green | Phase 4 |
| 6.2 | Manual UI checklist | Signed off | Phase 5 |
| 6.3 | Persistence restart test (PostgreSQL) | Data survives | Phase 0, 2 |
| 6.4 | Run `commands/review-code.md` | Findings in ai-review-notes | All phases |
| 6.5 | Run `commands/review-spec.md` | No spec/code drift | All phases |
| 6.6 | Document AI mistakes | `docs/ai-review-notes.md` | 6.4 |

---

## Task Order Diagram

```
Phase 0 (Setup)
    ↓
Phase 1 (State Machine + Tests)
    ↓
Phase 2 (Entities + Repository)
    ↓
Phase 3 (Service + Exceptions)
    ↓
Phase 4 (Controller + API Tests)
    ↓
Phase 5 (Frontend)
    ↓
Phase 6 (Review + Fix)
```

---

## Out of Scope (Do Not Implement)

- User authentication / RBAC
- Pagination
- Comment edit/delete
- Email notifications
- Audit log / event sourcing
- Kubernetes deployment

---

## Definition of Done

All items in `spec/requirements.md` acceptance criteria are met, traceability matrix is updated with actual class names, all planned tests pass, and `docs/ai-review-notes.md` contains ≥ 3 documented AI mistakes from review phase.
