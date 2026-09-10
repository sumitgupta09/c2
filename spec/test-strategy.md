# Test Strategy Specification

**Version:** 1.0

---

## 1. Test Pyramid

```
        ┌─────────────┐
        │  Manual UI  │  Few, checklist-based
        ├─────────────┤
        │ Integration │  API + DB (H2)
        ├─────────────┤
        │    Unit     │  State machine (no Spring)
        └─────────────┘
```

---

## 2. Unit Tests

### TicketStatusMachineTest
- **Scope:** Pure Java, no Spring context
- **Technique:** `@ParameterizedTest` with transition matrix
- **Covers:** SM V1–V6 (valid), SM I1–I8 (invalid)
- **Naming:** `allowsTransition_fromX_toY`, `rejectsTransition_fromX_toY`

---

## 3. Integration Tests

### TicketApiIntegrationTest
- **Framework:** `@SpringBootTest` + `@AutoConfigureMockMvc`
- **Profile:** `test` (H2 in-memory)
- **Isolation:** `create-drop` DDL per test class or `@Transactional` rollback

| Test Case | Requirement | Expected |
|-----------|-------------|----------|
| createTicket | FR-01 | 201, status OPEN |
| listTickets | FR-02 | 200, array |
| getTicket | FR-03 | 200 with comments |
| updateFields | FR-04–07 | 200, persisted |
| addComment | FR-08 | 201, comment in response |
| searchByKeyword | FR-09 | Only matching tickets |
| filterByStatus | FR-10 | Only matching status |
| validTransitionChain | SM V1–V5 | OPEN→IN_PROGRESS→RESOLVED→CLOSED |
| invalidTransition | SM I1 | 409 with message |
| blankTitle | VR-01 | 400 with fieldErrors.title |
| notFound | VR-09 | 404 |

---

## 4. Persistence Test (Manual or Integration)

| Test | Steps | Expected |
|------|-------|----------|
| Data survives restart | Create ticket via API → stop app → start app → GET ticket | 200, same data |

*Runtime DB: PostgreSQL. May use Testcontainers in CI if available.*

---

## 5. Frontend Manual Checklist

| # | Scenario | Pass? |
|---|----------|-------|
| M1 | Create ticket from UI | ☐ |
| M2 | List shows new ticket | ☐ |
| M3 | View ticket detail | ☐ |
| M4 | Update title, description, priority, assignee | ☐ |
| M5 | Valid status transition via dropdown | ☐ |
| M6 | Invalid status shows 409 error message | ☐ |
| M7 | Search finds ticket by keyword | ☐ |
| M8 | Status filter excludes non-matching | ☐ |
| M9 | Add comment appears in list | ☐ |
| M10 | Validation error shown on empty title | ☐ |
| M11 | Restart backend, data still present | ☐ |

---

## 6. Test Data Management

- Integration tests create their own data via API calls (no shared fixtures file)
- No production data in tests
- H2 never used for runtime manual testing of persistence requirement

---

## 7. Commands

```bash
# Backend unit + integration
cd backend && mvn test

# Frontend build (type check)
cd frontend && npm run build
```

---

## 8. Coverage Goals

| Area | Minimum |
|------|---------|
| State machine transitions | 100% of defined valid + invalid |
| API endpoints | All 5 endpoints, happy + key error paths |
| Validation | At least one field per DTO type |
| Acceptance criteria | Each AC has ≥ 1 automated or manual test |

---

## 9. AI Test Generation

Use `commands/generate-tests.md` to generate tests from this spec, not from implementation details alone. Tests should fail if implementation drifts from `spec/state-machine.md` or `spec/api-contract.md`.
