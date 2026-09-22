# Spec-Kit / OpenSpec Workflow Mapping

This project uses **manual Spec-Kit-equivalent artifacts** (no CLI required).

| Spec-Kit step | Command | This repo |
|---------------|---------|-----------|
| Init | `specify init` | `.constitution.md`, `rules/`, `commands/`, `skills/` |
| Constitution | `.constitution.md` | `.constitution.md` |
| Specify | `/specify` | `spec/requirements.md`, `docs/requirements-analysis.md` |
| Plan | `/plan` | `spec/architecture.md`, `spec/api-contract.md`, … |
| Tasks | `/tasks` | `docs/implementation-plan.md`, `.specify/tasks.md` |
| Implement | `/implement` | `backend/`, `frontend/` |
| Review | manual | `commands/review-code.md`, `commands/self-critique.md` |

See `.specify/tasks.md` for task completion status.
