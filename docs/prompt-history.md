# Prompt History

Record of AI prompts for this project. Raw SpecStory transcripts: `.specstory/history/` (when extension installed).

---

## 2026-09-10 — Initial scaffold (exploratory)
**Prompt:** Build Support Ticket Management System with spec-driven development (Java 21 Spring Boot + Next.js).
**Outcome:** Initial backend + frontend scaffold.
**AI mistakes:** See `docs/ai-review-notes.md` entries 1–5.

---

## 2026-09-10 — Planning phase (assignment deliverable)
**Prompt:** ONLY Requirement Analysis, Specification, AI Steering, Implementation Planning. No implementation.
**Files:** `spec/*`, `docs/requirements-analysis.md`, `rules/`, `commands/`, `docs/implementation-plan.md`
**Outcome:** Phase 1 planning complete.

---

## 2026-09-10 — Implementation: core ticket system
**Prompt:** Build create/list/view/update/comments/search/filter with state machine and validation.
**Chain:** spec → `TicketStatusMachine` → service → controller → integration tests → Next.js pages.
**Outcome:** Core acceptance criteria met.

---

## 2026-09-10 — Auth + corporate teams
**Prompt:** Login roles (admin, team lead, agent), auto-assign by ticket type, Acme Corp seed data.
**Outcome:** JWT auth, `SupportTeam`, `DataSeeder`, corporate routing.

---

## 2026-09-10 — Public portal
**Prompt:** Anyone can raise request and track by ID; only staff resolve tickets.
**Outcome:** Public `/`, `/tickets/new`; staff `/dashboard`; public GET/POST API.

---

## 2026-09-10 — Auto priority by type
**Prompt:** Priority decided by ticket type; admin can override.
**Outcome:** Removed priority from create form; `resolvePriority()` in `TicketAssignmentService`.

---

## 2026-09-10 — Compliance gap closure
**Prompt:** Close gaps: traceability, MCP server, constitution, persistence test, git/PR, prompt engineering artifacts.
**Files:** `.constitution.md`, `mcp-server/`, `PersistenceIntegrationTest`, `prompts/`, `commands/self-critique.md`
**Outcome:** Course compliance artifacts added.

---

## 2026-09-10 — Full requirements audit
**Prompt:** Audit every requirement; fix gaps; re-verify until done.
**Chain:** audit → fix persistence test → run mvn test → PostgreSQL verify → specstory export
**Outcome:** 29 tests pass; `verify-postgres.sh` OK; verification log written.

---

## 2026-09-10 — Complete partially done items
**Prompt:** do partially done things also
**Chain:** postgres verify → specstory history (37 files) → `.specify/` tasks → PR evidence
**Outcome:** All practical compliance gaps closed. PR push requires remote + `gh` CLI.

---

## SpecStory raw transcripts

37 prompt files exported to `.specstory/history/` from Cursor agent transcript (2026-09-10).

---

## Template for future entries

```markdown
## YYYY-MM-DD — [Topic]
**Prompt:** ...
**Files referenced:** @spec/..., @rules/...
**Chain step:** [specify|plan|tasks|implement|review]
**Outcome:** ...
**Self-critique:** [link to ai-review-notes if any]
```
