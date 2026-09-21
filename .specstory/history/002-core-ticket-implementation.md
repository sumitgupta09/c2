# 002 — Core Ticket System Implementation

**Date:** 2026-09-10  
**Stage:** Implementation → Testing  
**Inputs:** `@spec/requirements.md` `@spec/api-contract.md` `@spec/state-machine.md` `@spec/data-model.md` `@rules/java-springboot.md` `@rules/api-standards.md` `@rules/testing.md`

---

## Role
You are implementing a Spec-Driven Support Ticket API and UI. You follow specs; you do not invent conflicting behaviour.

## Goal
Deliver the core ticket system end-to-end so acceptance criteria for CRUD, search, filter, validation, errors, and state machine are provably met.

## Hard constraints
- Implement **one layer / one task at a time** (do not rewrite the whole app in one shot)
- State machine lives in **service/domain** (`TicketStatusMachine`) — never UI-only
- Controllers return **DTOs**, never JPA entities
- Invalid status transitions → **HTTP 409** with `{ message, fieldErrors }`
- Validation failures → **HTTP 400** with fieldErrors
- Persistence must survive restart (dev DB file or equivalent)
- No secrets committed

## Implementation order (strict)
1. Domain entities + enums  
2. `TicketStatusMachine` + pure unit tests (valid + invalid matrix)  
3. Repository + service (create/update/comment/search/filter)  
4. Controllers + GlobalExceptionHandler  
5. API integration tests (MockMvc) including 409 cases  
6. Next.js typed client + pages: create, list, detail, comments, search, status filter  
7. Wire meaningful UI errors from API `message` / `fieldErrors`

## Definition of Done
- [ ] Create / list / view / update / comment / search / filter work via API and UI  
- [ ] Valid transitions succeed; invalid transitions return 409  
- [ ] Unit + integration tests for state machine pass  
- [ ] UI shows validation and conflict errors clearly  
- [ ] Traceability updated for FR-01…FR-13  

## Output format
For each task completed: files changed, tests run + results, leftover risks.
