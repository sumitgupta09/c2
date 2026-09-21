# Prompt History

High-signal prompts used for Spec-Driven Development of this Support Ticket Management System.

**Raw SpecStory copies:** `.specstory/history/`  
**AI mistakes evidence:** `docs/ai-review.md`

---

## How these prompts are written (standard)

Every prompt includes:
1. **Role** — who the AI should be  
2. **Goal** — single clear outcome  
3. **Hard constraints** — what not to do  
4. **Inputs** — which `@spec` / `@rules` to attach  
5. **Ordered steps** — workflow stage discipline  
6. **Definition of Done** — checklist  
7. **Output format** — what to report back  

Never: “build the complete application” as a first prompt.

---

## SpecStory index

| # | SpecStory file | Stage | One-line intent |
|---|----------------|-------|-----------------|
| 001 | `001-planning-spec-driven-kickoff.md` | Spec / Plan | Specs + steering only |
| 002 | `002-core-ticket-implementation.md` | Implement / Test | Core ticket system |
| 003 | `003-auth-roles-auto-assign.md` | Implement | JWT + roles |
| 004 | `004-public-portal-raise-track.md` | Implement / UI | Public raise/track |
| 005 | `005-auto-priority-by-type.md` | Implement | Auto priority |
| 006 | `006-corporate-seed-data.md` | Data | Corporate demo seed |
| 007 | `007-compliance-mcp-prompts.md` | Compliance | Spec-Kit / MCP / prompts |
| 008 | `008-full-requirements-audit.md` | Audit / Fix | Fix until green |
| 009 | `009-complete-partial-items.md` | Fix | Close partials |
| 010 | `010-spec-driven-audit-completion.md` | Review / Fix | Spec drift + FE tests |
| 011 | `011-start-stop-scripts.md` | Ops | start.sh / stop.sh |
| 012 | `012-modern-glassmorphism-ui.md` | Frontend | Glass UI upgrade |

---

## Chronological outcomes (short)

### 2026-09-10 — Kickoff
Specs, rules, commands, constitution, implementation plan created (no full app yet).

### 2026-09-10 — Core build
Ticket CRUD, search/filter, state machine, tests, Next.js pages.

### 2026-09-10 — Auth + portal + priority + seed
JWT staff console; public raise/track; auto priority; corporate seeder.

### 2026-09-10 — Compliance + audit
MCP, prompts, persistence test, full requirement fix loop, partials closed.

### 2026-09-10 — Ops + UI
`start.sh`/`stop.sh`; modern glassmorphism UI.

### 2026-09-21 — Spec-driven audit
Spec alignment, FE unit tests, `docs/ai-review.md`, `docs/implementation-status.md`.

---

## Template for a new prompt

```markdown
# NNN — Title

**Stage:** Requirement | Specification | Plan | Implementation | Testing | Review | Fix
**Inputs:** @spec/... @rules/...

## Role
...

## Goal
...

## Hard constraints
- ...

## Steps
1. ...

## Definition of Done
- [ ] ...

## Output format
...
```
