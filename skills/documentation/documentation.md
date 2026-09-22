# Documentation Skill

**Purpose:** Guide AI in creating and maintaining project documentation throughout the spec-driven workflow.

---

## When to Use This Skill

- After completing a specification phase
- After implementing a feature (update specs if behaviour changed)
- After a review phase (record findings)
- After each AI prompt session (append prompt history)
- When onboarding a new team member or AI session

---

## Documentation Types

### 1. Architecture Documentation
**Location:** `spec/architecture.md`, `docs/requirements-analysis.md`

**AI should:**
- Describe layers and responsibilities
- Show how state machine fits into service layer
- Document database profile strategy (PostgreSQL vs H2)
- Update when a new component is added

**AI should NOT:**
- Document implementation details that duplicate code
- Add enterprise patterns not in scope

---

### 2. API Documentation
**Location:** `spec/api-contract.md`

**AI should:**
- Document every endpoint with request/response examples
- Specify HTTP status codes for success and error cases
- Keep enum values in sync with `domain/` enums
- Update before implementing controller changes

---

### 3. Decisions & Assumptions
**Location:** `docs/requirements-analysis.md` § N, O

**AI should record:**
- Ambiguities found in requirements
- Assumptions made (with rationale)
- Out-of-scope items explicitly excluded

**Format:**
```markdown
### Decision: [title]
**Context:** ...
**Decision:** ...
**Consequences:** ...
```

---

### 4. Implementation Notes
**Location:** `docs/implementation-plan.md`

**AI should:**
- Mark tasks complete as phases finish
- Note blockers or deviations from spec
- Link to relevant spec sections

---

### 5. Testing Documentation
**Location:** `spec/test-strategy.md`

**AI should:**
- Map tests to requirements (see `docs/requirements-traceability.md`)
- Document manual checklist items
- Update when new test cases are added

---

### 6. Review Findings
**Location:** `docs/ai-review-notes.md`

**AI should record:**
- Incorrect AI suggestions that were caught
- Why they were wrong
- What the correct approach is
- Reference to steering rule violated

**Minimum:** 3 documented mistakes during review phase (assignment requirement).

---

## Prompt History

**Location:** `docs/prompt-history.md`, `.specstory/history/`

After each AI session, append:
```markdown
## YYYY-MM-DD — [Topic]
**Prompt:** [summary or full prompt]
**Files referenced:** spec/..., rules/...
**Outcome:** [what was produced]
**AI mistakes caught:** [if any]
```

### SpecStory (Cursor / VS Code)
If the SpecStory extension is installed:
- Prompts auto-save to `.specstory/history/`
- Copy significant sessions to `docs/prompt-history.md` for repo visibility
- Do not fabricate entries — only record actual sessions

---

## Maintenance Rules

1. **Spec before code** — update spec first, then implement
2. **One source of truth** — if code and spec disagree, fix the one that's wrong (usually code)
3. **No stale docs** — if a feature is removed, remove its documentation
4. **Link, don't duplicate** — reference `spec/state-machine.md` instead of re-pasting transition tables
5. **Testable requirements** — every requirement in `spec/requirements.md` must have a test in traceability matrix

---

## Checklist Before Marking Phase Complete

- [ ] All spec files consistent with each other
- [ ] Requirements traceability matrix updated
- [ ] Prompt history entry added
- [ ] No fabricated documentation
- [ ] Assumptions and ambiguities explicitly stated
