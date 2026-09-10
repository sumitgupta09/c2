# Command: Generate Tests

Guide AI to generate tests from **requirements and specifications**, not from implementation details alone.

---

## Principle

> Tests should fail if the implementation drifts from the spec, even if the current code "works."

Generate tests by reading:
1. `spec/test-strategy.md`
2. `spec/state-machine.md`
3. `spec/api-contract.md`
4. `spec/requirements.md`
5. `docs/requirements-traceability.md`

Do **not** only read existing service/controller code and mirror it.

---

## Generation Order

### Step 1: State Machine Unit Tests
**Source:** `spec/state-machine.md` § Valid Transitions + § Invalid Transitions

```
Generate TicketStatusMachineTest with @ParameterizedTest:
- One test method for all VALID transitions (from spec table V1–V6)
- One test method for all INVALID transitions (from spec table I1–I8)
- No Spring context
```

### Step 2: API Integration Tests
**Source:** `spec/api-contract.md` + `spec/test-strategy.md` § 3

```
Generate TicketApiIntegrationTest with MockMvc:
- createTicket → 201, status OPEN
- listTickets → 200
- searchByKeyword → filtered results
- filterByStatus → filtered results
- updateFields → 200
- addComment → 201
- validTransitionChain → OPEN→IN_PROGRESS→RESOLVED→CLOSED
- invalidTransition → 409 with message containing "Invalid status transition"
- blankTitle → 400 with fieldErrors.title
- notFound → 404
```

### Step 3: Validation Tests
**Source:** `spec/requirements.md` § 3 Validation Rules

One negative test per DTO field with constraints.

---

## Test Naming Convention

```java
// Pattern: {action}_{condition}_{expectedResult}
createTicket_withValidPayload_returns201()
updateStatus_fromClosedToOpen_returns409()
searchTickets_withKeyword_returnsMatchingOnly()
```

---

## Test Data

- Minimal: one ticket title "Login issue", description "Cannot login"
- Create via API in test (Arrange-Act-Assert)
- No external JSON fixture files

---

## Assertions

| Assert | How |
|--------|-----|
| Status code | `status().isConflict()` |
| JSON field | `jsonPath("$.status").value("OPEN")` |
| Error message | `jsonPath("$.message").value(containsString("Invalid status transition"))` |
| Field error | `jsonPath("$.fieldErrors.title").exists()` |
| Array size | `jsonPath("$", hasSize(1))` |

---

## Anti-Patterns (Do NOT Generate)

- Tests that only assert `notNull`
- Tests that duplicate implementation logic (testing the test)
- Tests without corresponding requirement in traceability matrix
- Tests that depend on execution order
- Tests hitting PostgreSQL in CI (use H2 test profile)

---

## Verify

```bash
export JAVA_HOME=~/.sdkman/candidates/java/21.0.1-amzn
cd backend && mvn test -Dtest=TicketStatusMachineTest,TicketApiIntegrationTest
```

Then cross-check: every test method maps to a row in `docs/requirements-traceability.md`.

---

## Example AI Prompt

```
Read @spec/state-machine.md and @commands/generate-tests.md.
Generate TicketStatusMachineTest covering ALL valid and invalid transitions from the spec.
Do not read or copy from any existing implementation.
Tests should compile against a TicketStatusMachine interface you define based on the spec.
```
