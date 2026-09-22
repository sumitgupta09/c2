# 002 — Core Ticket System Implementation

**Date:** 2026-09-10  
**Stage:** Implementation → Testing  
**Inputs:** `@spec/requirements.md` `@spec/api-contract.md` `@spec/state-machine.md` `@spec/data-model.md` `@rules/java-springboot.md` `@rules/api-standards.md` `@rules/testing.md`

---

## Role
Backend and frontend engineer implementing from the existing specs.

## Goal
Ship the core ticket system end to end: CRUD, search, filter, validation, errors, and the state machine, with tests to prove it.

## Constraints
- One layer or one task at a time; do not rewrite the whole app in one pass
- State machine lives in service/domain (`TicketStatusMachine`) — never UI-only
- Controllers return DTOs, never JPA entities
- Invalid status transitions → HTTP 409 with `{ message, fieldErrors }`
- Validation failures → HTTP 400 with fieldErrors
- Persistence must survive restart (dev DB file or equivalent)
- No secrets committed

## Implementation order
1. Domain entities and enums  
2. `TicketStatusMachine` plus unit tests (valid and invalid matrix)  
3. Repository and service (create / update / comment / search / filter)  
4. Controllers and GlobalExceptionHandler  
5. API integration tests (MockMvc), including 409 cases  
6. Next.js typed client and pages: create, list, detail, comments, search, status filter  
7. Surface API `message` / `fieldErrors` clearly in the UI  

## Definition of Done
- [ ] Create / list / view / update / comment / search / filter work via API and UI  
- [ ] Valid transitions succeed; invalid transitions return 409  
- [ ] Unit and integration tests for the state machine pass  
- [ ] UI shows validation and conflict errors clearly  
- [ ] Traceability updated for FR-01…FR-13  

## Output format
For each completed task: files changed, tests run and results, remaining risks.
