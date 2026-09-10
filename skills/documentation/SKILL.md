---
name: documentation
description: Generate and maintain project documentation for the Support Ticket Management System. Use when writing specs, API docs, README updates, or prompt history entries.
---

# Documentation Skill

## When to Use
- After completing a feature: update relevant spec file
- After each AI prompt session: append to `docs/prompt-history.md`
- When API changes: update `spec/api-contract.md`

## Spec Files (source of truth)
| File | Purpose |
|------|---------|
| `spec/requirements.md` | User stories and acceptance criteria |
| `spec/architecture.md` | System components and tech choices |
| `spec/data-model.md` | Entities and relationships |
| `spec/api-contract.md` | REST endpoints and payloads |
| `spec/state-machine.md` | Ticket status transitions |
| `spec/ui-flow.md` | Frontend screens and interactions |
| `spec/test-strategy.md` | Test plan and coverage |

## Prompt History
Append each session to `docs/prompt-history.md`:
```markdown
## YYYY-MM-DD — <topic>
**Prompt:** ...
**Outcome:** ...
**AI mistakes caught:** ...
```

## README
Keep root `README.md` updated with setup, run, and test commands.
