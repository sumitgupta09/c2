# AI Review — Mistakes & Corrections

**Purpose:** Evidence that AI output is reviewed as an engineering assistant, not accepted blindly.  
**Related:** `docs/ai-review-notes.md` (earlier entries). This file is the assignment-required `docs/ai-review.md`.

---

## Issue 1 — State machine logic placed in the controller

| Field | Detail |
|-------|--------|
| **What AI suggested/generated** | Inline if/else status checks in `TicketController` on PATCH |
| **Why wrong** | Business rules belong in the service/domain layer; controller should only map HTTP. Violates `rules/java-springboot.md` and `spec/architecture.md` |
| **How detected** | Spec review (`commands/review-spec.md`) + code review against layering rules |
| **What changed** | Introduced `TicketStatusMachine`; `TicketService.updateTicket` calls `canTransition` and throws `InvalidStatusTransitionException` → 409 |
| **Caught by** | `TicketStatusMachineTest`, `TicketApiIntegrationTest.invalidTransitionReturns409` |

---

## Issue 2 — Returning JPA entities from REST endpoints

| Field | Detail |
|-------|--------|
| **What AI suggested/generated** | Return `Ticket` entity directly from controller methods |
| **Why wrong** | Leaks persistence model, lazy-load risk on `comments`, couples API to schema |
| **How detected** | `rules/api-standards.md` / `commands/review-code.md` |
| **What changed** | `TicketResponse` / `CommentResponse` DTOs only |
| **Caught by** | Integration tests asserting JSON shape; code review |

---

## Issue 3 — Overly permissive CORS (`*`) on every controller

| Field | Detail |
|-------|--------|
| **What AI suggested/generated** | `@CrossOrigin(origins = "*")` on each controller |
| **Why wrong** | Security risk; duplicated config |
| **How detected** | Security review of generated config |
| **What changed** | Central CORS config with explicit `http://localhost:3000` (dev) / env (prod) |
| **Caught by** | Manual review + `docs` security checklist |

---

## Issue 4 — Generic frontend errors that hide API messages

| Field | Detail |
|-------|--------|
| **What AI suggested/generated** | Show “Something went wrong” for any failed fetch |
| **Why wrong** | AC requires meaningful UI errors; 409 transition messages must surface |
| **How detected** | Acceptance criteria audit + UI flow spec |
| **What changed** | `parseApiError()` in `frontend/src/lib/parseApiError.ts`; banners + fieldErrors |
| **Caught by** | `parseApiError.test.ts`; manual/smoke UI |

---

## Issue 5 — Only happy-path state-machine tests

| Field | Detail |
|-------|--------|
| **What AI suggested/generated** | Test only OPEN → IN_PROGRESS → RESOLVED → CLOSED |
| **Why wrong** | Invalid transitions are a core requirement (CLOSED→OPEN must 409) |
| **How detected** | `spec/state-machine.md` vs generated tests |
| **What changed** | Parameterized invalid cases + API integration 409 test |
| **Caught by** | `TicketStatusMachineTest`, `TicketApiIntegrationTest` |

---

## Issue 6 — Spec drift after feature growth (2026-09-21 audit)

| Field | Detail |
|-------|--------|
| **What AI suggested/generated** | Left early specs unchanged after JWT/auth, ticket types, and public portal were added (e.g. `ui-flow.md` still said “No login screen” and `/` = staff list; `architecture.md` claimed `dev` = PostgreSQL; `requirements.md` listed Authentication as out of scope; `data-model.md` omitted `ticketType` / `User`) |
| **Why wrong** | Specs must describe the system under test; drift causes false “complete” claims and misleads future AI prompts |
| **How detected** | Spec-driven audit: compare `spec/*` to `backend` + `frontend` routes |
| **What changed** | Updated `spec/ui-flow.md`, `spec/architecture.md`, `spec/data-model.md`, `spec/requirements.md`, `spec/test-strategy.md` to match implementation |
| **Caught by** | Phase A specification review (this audit) |

---

## Issue 7 — Frontend “tests” claimed via smoke only

| Field | Detail |
|-------|--------|
| **What AI suggested/generated** | Treat `scripts/smoke-all.sh` + manual checklist as sufficient frontend testing |
| **Why wrong** | Audit requires automated coverage of important error/state flows; smoke proves reachability, not transition matrix / error parsing |
| **How detected** | Gap analysis: zero `*.test.ts` under `frontend/` |
| **What changed** | Added `statusTransitions.test.ts` + `parseApiError.test.ts`; `npm test` via Node 22 strip-types |
| **Caught by** | Testing requirements §9 of audit prompt |

---

## Summary

AI accelerated scaffolding, but **layering, security, error UX, and invalid transitions** required human rejection of first-pass suggestions. The 2026-09-21 audit additionally found **spec drift** and **missing frontend unit tests** — fixed without rewriting working backend logic.
