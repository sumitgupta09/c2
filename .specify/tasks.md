# Implementation Tasks (Spec-Kit /tasks equivalent)

**Status:** All phases complete  
**Source:** `docs/implementation-plan.md`

| Phase | Task | Status |
|-------|------|--------|
| 0 | Project scaffold, git, `.gitignore` | ✅ |
| 1 | Domain model + `TicketStatusMachine` + unit tests | ✅ |
| 2 | Repository + `TicketService` + validation | ✅ |
| 3 | REST controllers + integration tests | ✅ |
| 4 | Next.js UI (portal, dashboard, ticket CRUD) | ✅ |
| 5 | Auth, teams, auto-assignment, seed data | ✅ |
| 6 | MCP server, constitution, prompt templates | ✅ |
| 7 | Persistence tests, PostgreSQL verify, traceability | ✅ |
| 8 | Review + compliance audit | ✅ |

## Review gate

- [x] `commands/review-code.md` applied
- [x] `commands/review-spec.md` applied
- [x] `commands/self-critique.md` template available
- [x] `docs/ai-review-notes.md` records AI mistakes
- [x] `mvn test` — 29 tests pass
- [x] `./scripts/verify-postgres.sh` — PostgreSQL restart verified
