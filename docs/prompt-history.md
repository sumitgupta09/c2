# Prompt History

Descriptive record of the Spec-Driven Development prompts used to build the Gupta Corp Support Ticket Management System. Full prompt text lives in `.specstory/history/`. AI review notes: `docs/ai-review.md`.

---

## Program overview

Work followed a fixed progression: specify and plan first, then implement in thin slices, then audit and harden, then improve ops and UI. Each prompt below maps to one SpecStory file and one workflow stage.

| # | SpecStory file | Date | Stage |
|---|----------------|------|-------|
| 001 | `001-planning-spec-driven-kickoff.md` | 2026-09-10 | Requirement → Spec → Plan |
| 002 | `002-core-ticket-implementation.md` | 2026-09-10 | Implementation → Testing |
| 003 | `003-auth-roles-auto-assign.md` | 2026-09-10 | Implementation |
| 004 | `004-public-portal-raise-track.md` | 2026-09-10 | Implementation / UI |
| 005 | `005-auto-priority-by-type.md` | 2026-09-10 | Implementation |
| 006 | `006-corporate-seed-data.md` | 2026-09-10 | Implementation / Data |
| 007 | `007-compliance-mcp-prompts.md` | 2026-09-10 | Review / Compliance |
| 008 | `008-full-requirements-audit.md` | 2026-09-10 | Testing → Fix |
| 009 | `009-complete-partial-items.md` | 2026-09-10 | Fix |
| 010 | `010-spec-driven-audit-completion.md` | 2026-09-21 | Review → Fix |
| 011 | `011-start-stop-scripts.md` | 2026-09-10 | Operations |
| 012 | `012-modern-glassmorphism-ui.md` | 2026-09-10 | Frontend |

---

## 001 — Spec-Driven Kickoff

**SpecStory:** `.specstory/history/001-planning-spec-driven-kickoff.md`  
**Stage:** Requirement → Specification → Plan / Tasks  
**Role:** Senior software engineer and solution architect

Asked for repository setup only: requirements analysis, seven `spec/` documents, steering rules and commands, `.constitution.md`, and an implementation plan. Explicitly out of scope: Java backend, Next.js frontend, and any feature controllers or services. Product and state machine (OPEN → IN_PROGRESS → RESOLVED → CLOSED, plus CANCELLED paths; invalid reopen → 409) had to appear consistently across specs.

**Outcome:** Specs, rules, commands, constitution, and plan created with no application feature code.

---

## 002 — Core Ticket System Implementation

**SpecStory:** `.specstory/history/002-core-ticket-implementation.md`  
**Stage:** Implementation → Testing  
**Role:** Backend and frontend engineer  
**Inputs:** requirements, api-contract, state-machine, data-model specs; Java/API/testing rules

Asked to ship CRUD, search, filter, validation, errors, and the status state machine end to end, one layer at a time. Domain machine in service (`TicketStatusMachine`), DTOs at the API boundary, 409 for invalid transitions, 400 for validation, persistence across restart, unit and MockMvc tests, then Next.js pages that show API errors clearly.

**Outcome:** Core ticket API and UI with state-machine tests and FR-01…FR-13 traceability updates.

---

## 003 — Staff Auth, Roles, and Assignment Rules

**SpecStory:** `.specstory/history/003-auth-roles-auto-assign.md`  
**Stage:** Implementation  
**Role:** Engineer extending the ticket system into a staff desk

Asked for JWT login and roles `ADMIN`, `TEAM_ADMIN`, `AGENT`, with authorization on the backend (not UI-only). Public create/track had to keep working. Demo users allowed in seeder/docs only.

**Outcome:** Staff login, protected mutations, 401/403 behaviour, documented demo accounts.

---

## 004 — Public Portal: Raise Request and Track by ID

**SpecStory:** `.specstory/history/004-public-portal-raise-track.md`  
**Stage:** Implementation / UI  
**Role:** Frontend engineer  
**Inputs:** ui-flow, api-contract

Asked for a Help Center flow: anonymous raise and track-by-id; ticket type drives auto-assign; `OTHER` goes to main admin; staff resolve stays behind `/dashboard` login. Spec updates required if routes changed.

**Outcome:** Public `/`, `/tickets/new`, `/tickets/[id]`; staff resolve remained JWT-protected.

---

## 005 — Auto Priority From Ticket Type

**SpecStory:** `.specstory/history/005-auto-priority-by-type.md`  
**Stage:** Implementation  
**Role:** Backend engineer

Asked to remove customer-facing priority and derive defaults from ticket type in one service method. Admins keep override on update. Mapping must not be duplicated in controller and frontend; tests for at least two type→priority cases.

**Outcome:** Create form/DTO without priority field; defaults and admin override verified by tests.

---

## 006 — Corporate Support Desk Seed Data

**SpecStory:** `.specstory/history/006-corporate-seed-data.md`  
**Stage:** Implementation / Data  
**Role:** Platform engineer

Asked for realistic teams (Support Desk, IT, Database, HR, Finance, Accounts), hierarchy (main admin, team leads, agents), sample tickets across types, BCrypt hashing, and idempotent seeding. Demo passwords only in seeder and README.

**Outcome:** Demo-ready users and tickets with README logins and a clear seed log line.

---

## 007 — Compliance Artefacts: Spec-Kit, Prompts, MCP

**SpecStory:** `.specstory/history/007-compliance-mcp-prompts.md`  
**Stage:** Review / Compliance  
**Role:** Engineer closing compliance gaps with evidence

Asked to verify constitution, specs, tasks, review commands, prompt artefacts, MCP tools (2–3), Cursor config, traceability, and testing evidence. Empty folders do not count; prefer real docs/tests over cosmetic README claims.

**Outcome:** File-level evidence for Spec-Kit, prompt history, MCP, and Requirement → Spec → Code → Test mapping.

---

## 008 — Full Requirements Audit → Fix → Re-Verify

**SpecStory:** `.specstory/history/008-full-requirements-audit.md`  
**Stage:** Testing → Review → Fix  
**Role:** Engineer accountable for practical requirements

Asked to loop: inspect evidence, mark DONE / PARTIALLY DONE / NOT DONE, apply the smallest fix, re-run tests, re-audit until green. No rebuild-from-scratch; evidence must include commands and results.

**Outcome:** Backend tests green, invalid transitions covered, persistence restart verified, final checklist with evidence.

---

## 009 — Close Partially Completed Items

**SpecStory:** `.specstory/history/009-complete-partial-items.md`  
**Stage:** Fix  
**Role:** Engineer finishing incomplete compliance work

Asked to close remaining PARTIAL items: PostgreSQL verification, SpecStory/prompt history completeness, `.specify/tasks.md`, PR evidence, weak tests or missing traceability. No fabricated history or screenshots.

**Outcome:** PARTIAL list cleared or justified; artefacts linked; tests still passing.

---

## 010 — Spec-Driven Audit and Completion

**SpecStory:** `.specstory/history/010-spec-driven-audit-completion.md`  
**Stage:** Inspect → Spec update → Fix → Test → Report  
**Date:** 2026-09-21  
**Role:** Lead engineer

Asked for a full Spec-Driven audit without rebuilding: gap analysis, fix spec drift before behaviour changes, incremental fixes, tests, real AI-mistake notes, and `docs/implementation-status.md`. Verify rules, seven specs, SpecStory, state machine/409, persistence, validation, UI errors, and no committed secrets.

**Outcome:** Spec alignment, frontend unit tests, `docs/ai-review.md`, and implementation status report.

---

## 011 — Start/Stop Local Dev Scripts

**SpecStory:** `.specstory/history/011-start-stop-scripts.md`  
**Stage:** Operations  
**Role:** Engineer improving local DX

Asked for `start.sh` / `stop.sh`: free ports, start backend then frontend, wait for health, print URLs and demo login, force Java 21 via sdkman, fail fast with log tails. No secrets; Docker not required for default H2-file path.

**Outcome:** Reliable one-command start/stop documented in README.

---

## 012 — Modern Glassmorphism Portal UI

**SpecStory:** `.specstory/history/012-modern-glassmorphism-ui.md`  
**Stage:** Frontend design upgrade  
**Role:** Frontend engineer

Asked for a Gupta Corp branded glass UI (cards, depth, atmospheric background) while keeping create, track, login, and dashboard behaviour. Preserve API error parsing and backend state-machine ownership; smoke-test critical routes.

**Outcome:** Updated portal pages with centralized visual variables and successful smoke routes.

---

## Chronological summary

1. **2026-09-10** — Specs and steering only (001).  
2. **2026-09-10** — Core tickets + tests (002).  
3. **2026-09-10** — Auth, public portal, auto priority, corporate seed (003–006).  
4. **2026-09-10** — Compliance artefacts, full audit loop, close partials (007–009).  
5. **2026-09-10** — Start/stop scripts and glassmorphism UI (011–012).  
6. **2026-09-21** — Spec-driven audit completion (010).
