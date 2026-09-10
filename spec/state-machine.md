# State Machine Specification

**Version:** 1.0  
**Enforcement:** Backend service layer only

---

## Diagram

```
                    ┌─────────────┐
                    │    OPEN     │
                    └──────┬──────┘
                           │
              ┌────────────┼────────────┐
              │                         │
              ▼                         ▼
     ┌────────────────┐        ┌───────────────┐
     │  IN_PROGRESS   │        │  CANCELLED    │  (terminal)
     └────────┬───────┘        └───────────────┘
              │
     ┌────────┼────────┐
     │                 │
     ▼                 ▼
┌──────────┐    ┌───────────────┐
│ RESOLVED │    │  CANCELLED    │
└────┬─────┘    └───────────────┘
     │
     ▼
┌──────────┐
│  CLOSED  │  (terminal)
└──────────┘
```

---

## Transition Table

| From | Allowed To |
|------|------------|
| OPEN | IN_PROGRESS, CANCELLED |
| IN_PROGRESS | RESOLVED, CANCELLED |
| RESOLVED | CLOSED |
| CLOSED | *(none — terminal)* |
| CANCELLED | *(none — terminal)* |

---

## Valid Transitions (Test Cases)

| # | From | To | Result |
|---|------|-----|--------|
| V1 | OPEN | IN_PROGRESS | Allow |
| V2 | OPEN | CANCELLED | Allow |
| V3 | IN_PROGRESS | RESOLVED | Allow |
| V4 | IN_PROGRESS | CANCELLED | Allow |
| V5 | RESOLVED | CLOSED | Allow |
| V6 | OPEN | OPEN | Allow (no-op) |

---

## Invalid Transitions (Must Reject with 409)

| # | From | To | Example |
|---|------|-----|---------|
| I1 | CLOSED | OPEN | Reopen closed ticket |
| I2 | RESOLVED | OPEN | Roll back resolution |
| I3 | CANCELLED | OPEN | Reopen cancelled |
| I4 | OPEN | RESOLVED | Skip IN_PROGRESS |
| I5 | OPEN | CLOSED | Skip workflow |
| I6 | RESOLVED | IN_PROGRESS | Roll back |
| I7 | CLOSED | CANCELLED | Modify terminal state |
| I8 | CANCELLED | CLOSED | Modify terminal state |

---

## HTTP Response for Invalid Transition

**Status:** `409 Conflict`

**Body:**
```json
{
  "message": "Invalid status transition from CLOSED to OPEN",
  "fieldErrors": {}
}
```

---

## Implementation Contract

```java
// TicketStatusMachine
boolean canTransition(TicketStatus from, TicketStatus to);
Set<TicketStatus> allowedTransitions(TicketStatus from);
```

- `canTransition(from, from)` returns `true` (no-op).
- Service calls `canTransition` before persisting status change.
- Controller does **not** contain transition logic.
- Frontend may hide invalid options for UX but **must not** be trusted.

---

## Frontend UX (Non-Authoritative)

On ticket detail page, status dropdown shows:
- Current status
- `allowedTransitions(currentStatus)`

If user somehow submits invalid status (e.g. via API tool), backend returns 409 and UI displays `message`.
