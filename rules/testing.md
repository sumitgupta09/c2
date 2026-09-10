# Testing Guidelines

## Test Types

| Type | Scope | Framework | DB |
|------|-------|-----------|-----|
| Unit | Single class, no Spring | JUnit 5 | None |
| Integration | Full Spring context + HTTP | JUnit 5 + MockMvc | H2 (test profile) |
| Repository | Optional, if custom query complex | `@DataJpaTest` | H2 |
| Manual UI | End-to-end via browser | Checklist | PostgreSQL |

---

## Unit Testing
- Test `TicketStatusMachine` without Spring context
- Use `@ParameterizedTest` + `@MethodSource` for transition matrix
- Assert boolean results or exception types — not implementation details
- Fast: < 1 second total

---

## Integration Testing
- `@SpringBootTest` + `@AutoConfigureMockMvc`
- `@ActiveProfiles("test")` — always use H2, never PostgreSQL in CI unit runs
- Test full request → response cycle including JSON body and status code
- Use `ObjectMapper` to build/parse JSON

---

## REST API Testing
- Verify status codes: 200, 201, 400, 404, 409
- Verify response JSON structure matches `spec/api-contract.md`
- Verify error envelope: `{ message, fieldErrors }`
- Test both happy path and rejection cases

---

## State-Machine Testing
- **Unit:** Every valid transition in `spec/state-machine.md` § Valid
- **Unit:** Every invalid transition in `spec/state-machine.md` § Invalid
- **Integration:** Full chain OPEN → IN_PROGRESS → RESOLVED → CLOSED via PATCH
- **Integration:** CLOSED → OPEN returns 409

---

## Positive Tests
- Create ticket with all fields
- Create ticket without assignee
- List, search, filter return expected results
- Update each field independently
- Add comment

---

## Negative Tests
- Blank title on create → 400
- Invalid status transition → 409
- Non-existent ticket ID → 404
- Invalid enum value → 400

---

## Validation Tests
- At least one test per DTO with `@NotBlank` / `@NotNull` violation
- Assert `fieldErrors` contains correct field name

---

## Test Naming
```
// Unit
allowsTransition_fromOpen_toInProgress()
rejectsTransition_fromClosed_toOpen()

// Integration
createTicket_returns201_withOpenStatus()
updateStatus_invalidTransition_returns409()
```

---

## Test Isolation
- H2 `create-drop` or `@Transactional` rollback per test
- No shared mutable state between tests
- No dependency on test execution order

---

## Test Data Management
- Create data via API calls in test setup (Arrange-Act-Assert)
- No external fixture files for assessment scope
- Use realistic but minimal data (one ticket, one comment)

---

## Commands
```bash
cd backend && mvn test
cd backend && mvn test -Dtest=TicketStatusMachineTest
cd backend && mvn test -Dtest=TicketApiIntegrationTest
```

---

## What NOT to Test
- Framework behaviour (Spring's `@Valid` itself)
- Trivial getters/setters
- Generated code
- Private methods directly (test via public API)
