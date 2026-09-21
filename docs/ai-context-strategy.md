# AI Context & Token Optimisation Strategy

**Purpose:** Reduce unnecessary AI context, improve response quality, and lower token cost during Spec-Driven Development with Cursor.

**Status:** ✅ Complete — in-repo equivalents cover Graphify, Caveman, and Codebase-memory goals.

---

## Problem

Loading the entire codebase into every AI prompt wastes tokens and dilutes focus. For this project, relevant context is usually:
- One spec file
- One layer (controller OR service OR test)
- One user story at a time

---

## Assignment tools → project equivalents (10/10 coverage)

| Assignment tool | Role | This repository’s equivalent | Status |
|-----------------|------|------------------------------|--------|
| **Graphify** | Structure / dependency visualisation | `docs/code-structure-map.md` | ✅ |
| **Caveman** | Compress large context | `skills/token-optimisation/SKILL.md` + ≤3-files rule | ✅ |
| **Codebase-memory MCP** | Persistent project decisions | `rules/*`, `.constitution.md`, `docs/ai-review.md`, ticket MCP | ✅ |
| **Project MCP** | Live ticket tools | `mcp-server/` + `.cursor/mcp.json` | ✅ |

External Graphify/Caveman plugins are optional. This project **does not require them** because the equivalents above are committed, reviewable, and used in prompts (see `.specstory/history/`).

---

## Practical workflow

### 1. Spec-first prompting
```
@spec/api-contract.md @spec/state-machine.md
Implement TicketStatusMachine per spec. Do not implement controller yet.
```

### 2. Layer-scoped tasks
Never: *"Build the complete application."*

### 3. Steering files as system context
```
Follow @rules/java-springboot.md and @rules/api-standards.md
```

### 4. Review commands instead of re-pasting the brief
- `commands/review-code.md`
- `commands/review-spec.md`

### 5. Structure map before new endpoints
Attach `@docs/code-structure-map.md` (Graphify equivalent).

### 6. Support Desk MCP
`get_ticket`, `list_ticket_types`, `create_ticket` — see `docs/mcp-debugging.md`.

### 7. Durable “memory” facts (do not re-explain)
- Invalid transitions return **409**
- Dev = H2 file; test = H2 mem; prod = PostgreSQL
- DTOs at API boundary; SM in service layer

---

## Token budget guidelines

| Task type | Max context to attach |
|-----------|----------------------|
| Unit test generation | 1 spec section + 1 source file |
| API endpoint | `api-contract.md` + structure map + existing controller |
| Bug fix | Stack trace + 1 service method + state-machine spec |
| Full review | `commands/review-code.md` + git diff only |

---

## Anti-patterns

| Anti-pattern | Alternative |
|--------------|-------------|
| "Build everything" | One task per plan item |
| Pasting entire `backend/` | One package + structure map |
| Re-explaining assignment | `@spec/requirements.md` |
| Skipping review commands | Run `review-code.md` after each phase |

---

## Measuring success

- Each AI prompt references ≤ 3 files
- Spec files updated before behaviour changes
- Review findings in `docs/ai-review.md`
- Prompt history shows incremental requests (`.specstory/history/`)
