# Few-Shot Prompt Examples

## Example A — Good (layer-scoped, spec-attached)

**User:**
```
@spec/state-machine.md @commands/generate-tests.md
Implement TicketStatusMachine only. Parameterized tests for all valid and invalid transitions.
No controller, no JPA entities.
```

**Why it works:** Single layer, spec cited, test command referenced, explicit exclusions.

---

## Example B — Bad → Good fix

**Bad:**
```
Build the support ticket app with React and whatever database is fastest.
```

**Good:**
```
@.constitution.md @spec/api-contract.md
Implement TicketController POST /api/tickets only.
Use existing TicketService.createTicket. Add MockMvc test expecting 201 and status OPEN.
```

---

## Example C — Self-critique chain

**Step 1 — Implement:**
```
@spec/ui-flow.md Implement dashboard search (debounced) calling GET /api/tickets?keyword=
```

**Step 2 — Critique:**
```
@commands/self-critique.md
Critique the dashboard search implementation you just wrote.
```

**Step 3 — Fix:**
```
Apply only the "Recommended fixes" from your critique. No other changes.
```

---

## Example D — Structured test generation

**User:**
```
@commands/generate-tests.md @spec/test-strategy.md
Generate TicketApiIntegrationTest methods:
- searchByKeyword
- filterByStatus  
- invalidTransitionReturns409
Do not read existing service implementation.
```

**Expected AI behaviour:** Creates tests from spec table, not by mirroring buggy code.
