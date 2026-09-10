# Command: Review Code

Reusable process for reviewing AI-generated or human-written code.

---

## Prerequisites

Attach to AI prompt:
- `rules/java-springboot.md`
- `rules/api-standards.md`
- `rules/testing.md`
- Relevant `spec/*.md` files
- Git diff of changes

---

## Review Steps

### 1. Architecture Compliance
- [ ] Controller contains no business logic
- [ ] State machine enforced in service layer only
- [ ] DTOs used at API boundary (no entity leakage)
- [ ] Constructor injection used throughout
- [ ] Package structure matches `rules/java-springboot.md`

### 2. API Contract Compliance
- [ ] Endpoints match `spec/api-contract.md` (paths, methods, status codes)
- [ ] Request/response shapes match spec examples
- [ ] Error format: `{ message, fieldErrors }`
- [ ] 409 returned for invalid status transitions

### 3. State Machine Compliance
- [ ] `TicketStatusMachine` matches `spec/state-machine.md` transition table
- [ ] All invalid transitions rejected
- [ ] Same-status update allowed (no-op)

### 4. Validation
- [ ] `@Valid` on all write endpoints
- [ ] Jakarta annotations on DTOs match `spec/requirements.md` validation rules
- [ ] Field errors mapped correctly in exception handler

### 5. Security
- [ ] No hardcoded passwords, API keys, or tokens
- [ ] `.gitignore` covers `.env`, credentials
- [ ] CORS not set to `*` in production config

### 6. Testing
- [ ] State machine unit tests cover full transition matrix
- [ ] Integration tests cover all endpoints
- [ ] Negative tests for 400, 404, 409
- [ ] Tests use `test` profile (H2), not PostgreSQL

### 7. Maintainability
- [ ] No over-engineering (unnecessary abstractions)
- [ ] Consistent naming with existing code
- [ ] No dead code or commented-out blocks

---

## Output Format

```markdown
## Code Review — [date]

### Critical (must fix)
- ...

### Warning (should fix)
- ...

### Suggestion (nice to have)
- ...

### AI Mistakes Caught
- [Mistake]: [Why wrong] → [Fix applied]
```

Record AI mistakes in `docs/ai-review-notes.md`.

---

## Run Tests

```bash
export JAVA_HOME=~/.sdkman/candidates/java/21.0.1-amzn
cd backend && mvn test
cd frontend && npm run build
```

All tests must pass before review is complete.
