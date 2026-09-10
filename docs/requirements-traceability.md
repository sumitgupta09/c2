# Requirements Traceability Matrix

**Status key:** `Implemented` = verified in code and/or tests  
**Last updated:** 2026-09-10 (post-implementation)

| Req ID | Requirement | Specification | Implementation | Test / Verification | AC | Status |
|--------|-------------|---------------|----------------|---------------------|-----|--------|
| FR-01 | Create ticket | `spec/requirements.md`, `spec/api-contract.md` | `TicketController.create`, `TicketService.createTicket` | `TicketApiIntegrationTest.userCreatesTicketAutoAssignedByType`, `guestCanCreateTicketAndCheckStatusWithoutLogin` | AC-1 | Implemented |
| FR-02 | List tickets | `spec/api-contract.md` | `TicketController.list`, `TicketRepository.search` | `TicketApiIntegrationTest.listIncludesCreatedTickets` | AC-2 | Implemented |
| FR-03 | View ticket details | `spec/api-contract.md`, `spec/ui-flow.md` | `TicketController.get`, `/tickets/[id]` | `guestCanCreateTicketAndCheckStatusWithoutLogin` (GET by id) | AC-3 | Implemented |
| FR-04 | Update title | `spec/api-contract.md` | `TicketService.updateTicket` | `TicketApiIntegrationTest.updateTitleAndDescription` | AC-4 | Implemented |
| FR-05 | Update description | `spec/api-contract.md` | `TicketService.updateTicket` | `TicketApiIntegrationTest.updateTitleAndDescription` | AC-4 | Implemented |
| FR-06 | Update priority | `spec/api-contract.md` | `TicketService.updateTicket` (admin only) | `TicketApiIntegrationTest.onlyMainAdminCanChangePriority` | AC-4 | Implemented |
| FR-07 | Update assignee | `spec/api-contract.md` | `TicketService.updateTicket` | `TicketApiIntegrationTest.teamLeadCanReassignWithinTeam` | AC-5 | Implemented |
| FR-08 | Add comments | `spec/api-contract.md`, `spec/data-model.md` | `TicketController.addComment` | `TicketApiIntegrationTest.addComment` | AC-6 | Implemented |
| FR-09 | Search by keyword | `spec/api-contract.md` | `TicketRepository.search` | `TicketApiIntegrationTest.searchByKeyword` | AC-7 | Implemented |
| FR-10 | Filter by status | `spec/api-contract.md` | `TicketRepository.search` | `TicketApiIntegrationTest.filterByStatus` | AC-8 | Implemented |
| FR-11 | Persist data | `spec/architecture.md` | JPA + H2 file (dev), PostgreSQL (prod) | `PersistenceIntegrationTest.restartSurvives` | AC-11 | Implemented |
| FR-12 | Backend validation | `spec/requirements.md` | Jakarta Validation + `GlobalExceptionHandler` | `TicketApiIntegrationTest.blankTitleReturns400WithFieldErrors` | AC-12 | Implemented |
| FR-13 | UI meaningful errors | `spec/ui-flow.md` | `lib/api.ts`, alerts + `fieldErrors` on create/detail | Manual + smoke script | AC-13 | Implemented |
| BR-01 | Default status OPEN | `spec/state-machine.md` | `Ticket` default / create flow | `userCreatesTicketAutoAssignedByType` expects OPEN | AC-1 | Implemented |
| BR-03 | Reject invalid transitions | `spec/state-machine.md` | `TicketStatusMachine` → 409 | `TicketStatusMachineTest`, `invalidTransitionReturns409` | AC-10 | Implemented |
| SM-01–05 | Valid transitions | `spec/state-machine.md` | `TicketStatusMachine` | `TicketStatusMachineTest.validTransitions`, `validTransitionChain` | AC-9 | Implemented |
| SM-06–10 | Invalid transitions | `spec/state-machine.md` | Service throws 409 | `TicketStatusMachineTest.invalidTransitions` | AC-10 | Implemented |
| NFR-02 | PostgreSQL runtime | `spec/architecture.md` | `application-prod.yml`, `docker-compose.yml` | Manual with `prod` profile | AC-11 | Implemented |
| NFR-03 | H2 for tests | `spec/test-strategy.md` | `application-test.yml` | `@ActiveProfiles("test")` | TR-02 | Implemented |
| SEC-01 | No secrets committed | `rules/java-springboot.md` | `.gitignore`, `.env.example`, `${JWT_SECRET}` | `git grep` / review | AC-15 | Implemented |
| FE-01–09 | Frontend flows | `spec/ui-flow.md` | `/`, `/dashboard`, `/tickets/new`, `/tickets/[id]`, `/login` | `scripts/smoke-all.sh` | AC-1–8, AC-13 | Implemented |

## UI route note (spec drift resolved)

| Spec (original) | Implemented | Reason |
|-----------------|-------------|--------|
| `/` = staff ticket list | `/` = public portal, `/dashboard` = staff list | Public create + track without login |

## Test class mapping

| Test class | Requirements |
|------------|--------------|
| `TicketStatusMachineTest` | SM-01–SM-11 |
| `TicketApiIntegrationTest` | FR-01–FR-12, BR-01, BR-03 |
| `PersistenceIntegrationTest` | FR-11, AC-11 |
| `scripts/smoke-all.sh` | End-to-end API + frontend health |

## Review & workflow artifacts

| Artifact | Purpose |
|----------|---------|
| `.constitution.md` | Spec-Kit constitution |
| `commands/review-code.md` | Code review checklist |
| `commands/review-spec.md` | Spec review checklist |
| `commands/self-critique.md` | AI self-critique template |
| `docs/ai-review-notes.md` | Recorded AI mistakes |
| `mcp-server/` | MCP tools for ticket API |
