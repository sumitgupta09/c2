# AI Context & Token Optimisation Strategy

**Purpose:** Reduce unnecessary AI context, improve response quality, and lower token cost during spec-driven development with Cursor.

**Status:** Strategy documented. **MCP server installed** (`mcp-server/`, `.cursor/mcp.json`). Graphify/Caveman/Codebase-memory are optional external tools — not bundled.

---

## Problem

Loading the entire codebase into every AI prompt wastes tokens and dilutes focus. For this project, relevant context is usually:
- One spec file
- One layer (controller OR service OR test)
- One user story at a time

---

## Recommended Tools (Assignment)

| Tool | Role | When to Use |
|------|------|-------------|
| **Graphify** | Visualise code structure / dependencies | Before refactoring; understanding package relationships |
| **Caveman** | Compress/summarise code context | When pasting large files into prompts |
| **Codebase-memory MCP** | Persistent memory of project decisions | Recall assumptions, API conventions, past review findings |

*Do not claim these are installed unless present in the environment.*

---

## Practical Workflow (With or Without Plugins)

### 1. Spec-first prompting
Before asking AI to write code, attach only the relevant spec:
```
@spec/api-contract.md @spec/state-machine.md
Implement TicketStatusMachine per spec. Do not implement controller yet.
```

### 2. Layer-scoped tasks
Never prompt: *"Build the complete application."*

Instead:
- Phase 1: `TicketStatusMachine` + unit tests
- Phase 2: `TicketService` + repository
- Phase 3: `TicketController` + integration tests
- Phase 4: One frontend page at a time

### 3. Use project steering files as system context
Cursor rules in `rules/` are loaded automatically. Reference them instead of re-explaining conventions:
```
Follow @rules/java-springboot.md and @rules/api-standards.md
```

### 4. Use commands as review checklists
After AI generates code, run the process in:
- `commands/review-code.md`
- `commands/review-spec.md`

This avoids re-pasting the full assignment brief.

### 5. Targeted search before broad reads
| Need | Action |
|------|--------|
| Find state machine | Grep `TicketStatus` / read `spec/state-machine.md` only |
| Find API shape | Read `spec/api-contract.md` only |
| Review test gaps | Read `spec/test-strategy.md` + `commands/generate-tests.md` |

### 6. Support Desk MCP (installed)
Use `get_ticket`, `list_ticket_types`, `create_ticket` via Cursor MCP panel instead of pasting API responses into prompts. See `docs/mcp-debugging.md`.

### 7. Codebase-memory MCP (if available)
Store durable facts once:
- "Invalid transitions return 409"
- "PostgreSQL for runtime, H2 for tests"
- "DTOs are records in `dto/` package"

Subsequent prompts retrieve memory instead of re-loading specs.

### 8. Graphify (if available)
Generate a dependency graph of `controller → service → repository` before asking AI to add a new endpoint. Prevents duplicate classes or wrong-layer logic.

### 9. Caveman (if available)
When you must include a large file, compress it to signatures + key methods before prompting.

---

## Token Budget Guidelines

| Task Type | Max Context to Attach |
|-----------|----------------------|
| Unit test generation | 1 spec section + 1 source file |
| API endpoint | `api-contract.md` + existing controller |
| Bug fix | Stack trace + 1 service method + state-machine spec |
| Full review | `commands/review-code.md` + git diff only |

---

## Anti-Patterns

| Anti-Pattern | Why It Fails | Alternative |
|--------------|--------------|-------------|
| "Build everything" | Huge diff, inconsistent architecture | One task per plan item |
| Pasting entire `backend/` | Token waste | One package at a time |
| Re-explaining assignment | Duplicates `spec/requirements.md` | `@spec/requirements.md` |
| Skipping review commands | AI mistakes accumulate | Run `review-code.md` after each phase |

---

## Measuring Success

- Each AI prompt references ≤ 3 files
- Spec files updated before code changes
- Review findings recorded in `docs/ai-review-notes.md`
- Prompt history in `docs/prompt-history.md` shows incremental, not monolithic, requests
