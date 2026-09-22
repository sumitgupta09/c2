# Skill: Token Optimisation (Caveman / memory equivalents)

Use this skill whenever prompting AI on this repository.

## Equivalents to assignment tools

| External tool | In-repo equivalent | How to use |
|---------------|--------------------|------------|
| Graphify | `docs/code-structure-map.md` | Attach before refactors / new endpoints |
| Caveman (compress context) | This skill + “≤3 files per prompt” rule | Paste signatures only; never whole packages |
| Codebase-memory MCP | `rules/*`, `.constitution.md`, `docs/ai-review.md`, MCP ticket tools | Re-read rules instead of re-explaining |

## Always
- Attach ≤ 3 files per prompt
- Prefer `@spec/...` over pasting code
- One layer per task (controller OR service OR test)
- After generation, run `commands/review-code.md`

## Never
- “Build the complete application”
- Paste entire `backend/` or `frontend/`
- Re-paste the full assignment brief (use `@spec/requirements.md`)
