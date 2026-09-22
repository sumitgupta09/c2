# Command: Review Specification

Reusable process for reviewing specifications for completeness, consistency, and testability.

---

## When to Run

- After initial spec creation (before any implementation)
- After requirements change
- After implementation (check for spec/code drift)
- Before marking a phase complete

---

## Review Steps

### 1. Compleeness
- [ ] All 7 spec files exist in `spec/`
- [ ] `docs/requirements-analysis.md` covers sections A–P
- [ ] `docs/requirements-traceability.md` maps all FR/BR/SM items
- [ ] `docs/implementation-plan.md` has ordered tasks
- [ ] Every acceptance criterion in assignment is traceable

### 2. Consistency Cross-Check

| Check | Files to Compare |
|-------|------------------|
| Status enum values | `data-model.md` ↔ `api-contract.md` ↔ `state-machine.md` |
| Endpoints | `api-contract.md` ↔ `ui-flow.md` ↔ `architecture.md` |
| Validation rules | `requirements.md` ↔ `api-contract.md` ↔ `requirements-analysis.md` § D |
| Error codes | `api-contract.md` ↔ `state-machine.md` ↔ `api-standards.md` |
| Test coverage | `test-strategy.md` ↔ `requirements-traceability.md` |

### 3. State Machine
- [ ] Transition table is complete (all valid + invalid examples)
- [ ] 409 specified for invalid transitions
- [ ] Enforcement layer specified (service, not controller/frontend)
- [ ] Terminal states (CLOSED, CANCELLED) have no outbound transitions

### 4. Testability
- [ ] Every functional requirement has a test case in traceability matrix
- [ ] `test-strategy.md` covers unit, integration, and manual tests
- [ ] No requirement uses vague language ("should work well")

### 5. Scope Control
- [ ] No invented features (auth, pagination, notifications)
- [ ] Out-of-scope items explicitly listed
- [ ] Ambiguities documented with assumptions

### 6. AI Steering
- [ ] `rules/` files are actionable (not generic platitudes)
- [ ] `commands/` files have checklists
- [ ] `skills/documentation/documentation.md` covers all doc types

---

## Drift Detection (Post-Implementation)

If code exists, compare:
| Spec Says | Code Does | Action |
|-----------|-----------|--------|
| PostgreSQL runtime | H2 in dev | Update spec OR fix code |
| 409 on invalid transition | 400 returned | Fix code |
| Endpoint missing | Extra endpoint | Update spec OR remove endpoint |

**Default:** Spec is source of truth unless spec is wrong — then update spec first.

---

## Output Format

```markdown
## Spec Review — [date]

### Gaps Found
- ...

### Inconsistencies
- [file A] says X but [file B] says Y → Resolution: ...

### Ambiguities Needing Clarification
- ...

### Verdict
- [ ] Ready for implementation
- [ ] Needs revision (list items)
```
