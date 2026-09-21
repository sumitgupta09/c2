# 001 — Spec-Driven Kickoff

**Date:** 2026-09-10  
**Stage:** Requirement → Specification → Plan / Tasks  
**Do not:** Implement the application in this step

---

## Role
You are a Senior Software Engineer, Solution Architect, and Spec-Driven Development mentor working in Cursor.

## Goal
Prepare the repository to build a Support Ticket Management System using AI correctly — specifications and steering first, code later.

## Hard constraints
- Follow: Requirement → Specification → Plan / Tasks → (later) Implementation → Testing → Review → Fix
- **Do NOT** start by building the complete application
- **Do NOT** generate the full Java backend or Next.js frontend yet
- **Do NOT** create all controllers/services/repositories in this phase
- Prefer small, reviewable artefacts over large generated dumps
- One concern per artefact; keep specs consistent with each other

## Technology context (for specs only)
Java 21 · Spring Boot · PostgreSQL (runtime) / H2 (tests) · REST · Next.js · Cursor · GitHub Copilot

## Product context
Support Ticket Management System with:
create, list, view, update (title/description/priority/assignee), comments, keyword search, status filter, persistence, backend validation, meaningful UI errors.

## State machine (must appear in specs)
```
OPEN → IN_PROGRESS → RESOLVED → CLOSED
OPEN → CANCELLED
IN_PROGRESS → CANCELLED
```
Invalid examples that must be rejected by backend: CLOSED→OPEN, RESOLVED→OPEN, CANCELLED→OPEN.

## Deliverables (this phase only)
1. `docs/requirements-analysis.md` — ambiguities, assumptions, out-of-scope, acceptance mapping  
2. Seven specs under `spec/`: requirements, architecture, data-model, api-contract, state-machine, ui-flow, test-strategy  
3. AI steering: `rules/java-springboot.md`, `rules/testing.md`, `rules/api-standards.md`, `skills/documentation/`, `commands/review-code.md`, `commands/review-spec.md`, `commands/generate-tests.md`  
4. `.constitution.md` — non-negotiable project laws  
5. `docs/implementation-plan.md` — ordered tasks with dependencies and Definition of Done  

## Quality bar
- Specs must be useful and implementation-independent, but mutually consistent (enums, 409 behaviour, validation)
- Rules must be actionable for future AI prompts (not vague slogans)
- Cross-check state machine language across requirements, api-contract, and state-machine specs

## Definition of Done
- [ ] All deliverables above exist and are non-empty  
- [ ] No application feature code was added in this phase  
- [ ] Specs agree on status enums and invalid-transition → 409  
- [ ] Implementation plan breaks work into small tasks  

## Output format
Summarize what you created, list file paths, and call out any open ambiguities you documented (do not invent requirements silently).
