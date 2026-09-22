# 001 — Spec-Driven Kickoff

**Date:** 2026-09-10  
**Stage:** Requirement → Specification → Plan / Tasks  
**Out of scope:** Application implementation

---

## Role
Senior software engineer and solution architect.

## Goal
Set up the repository for a Support Ticket Management System: specifications and steering first, code later.

## Constraints
- Follow: Requirement → Specification → Plan / Tasks → (later) Implementation → Testing → Review → Fix
- Do not build the complete application in this step
- Do not generate the full Java backend or Next.js frontend yet
- Do not create all controllers, services, or repositories in this phase
- Prefer small, reviewable artefacts over large dumps
- One concern per artefact; keep specs consistent with each other

## Technology context (for specs only)
Java 21 · Spring Boot · PostgreSQL (runtime) / H2 (tests) · REST · Next.js · Cursor · GitHub Copilot

## Product context
Support Ticket Management System with: create, list, view, update (title / description / priority / assignee), comments, keyword search, status filter, persistence, backend validation, and clear UI errors.

## State machine (must appear in specs)
```
OPEN → IN_PROGRESS → RESOLVED → CLOSED
OPEN → CANCELLED
IN_PROGRESS → CANCELLED
```
Invalid transitions the backend must reject: CLOSED→OPEN, RESOLVED→OPEN, CANCELLED→OPEN.

## Deliverables (this phase only)
1. `docs/requirements-analysis.md` — ambiguities, assumptions, out of scope, acceptance mapping  
2. Seven specs under `spec/`: requirements, architecture, data-model, api-contract, state-machine, ui-flow, test-strategy  
3. Steering files: `rules/java-springboot.md`, `rules/testing.md`, `rules/api-standards.md`, `skills/documentation/`, `commands/review-code.md`, `commands/review-spec.md`, `commands/generate-tests.md`  
4. `.constitution.md` — non-negotiable project rules  
5. `docs/implementation-plan.md` — ordered tasks with dependencies and Definition of Done  

## Quality bar
- Specs are useful and implementation-independent, but mutually consistent (enums, 409 behaviour, validation)
- Rules are actionable, not vague slogans
- State machine language matches across requirements, api-contract, and state-machine specs

## Definition of Done
- [ ] All deliverables above exist and are non-empty  
- [ ] No application feature code was added in this phase  
- [ ] Specs agree on status enums and invalid-transition → 409  
- [ ] Implementation plan breaks work into small tasks  

## Output format
Summarize what was created, list file paths, and note any open ambiguities that were documented. Do not invent requirements silently.
