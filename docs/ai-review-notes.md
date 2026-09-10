# AI Review Notes

Documenting mistakes or incorrect suggestions caught during AI-assisted development.

## 1. Putting state machine logic in the controller
**AI suggestion:** Handle status transitions directly in `TicketController` with inline if/else.
**Why wrong:** Business rules belong in the service layer; controller should only route HTTP. Violates `rules/java-springboot.md`.
**Fix:** Created `TicketStatusMachine` component and enforced transitions in `TicketService`.

## 2. Returning JPA entities from REST endpoints
**AI suggestion:** Return `Ticket` entity directly from controller methods.
**Why wrong:** Exposes internal structure, causes lazy-loading issues with comments, couples API to persistence.
**Fix:** Use `TicketResponse` and `CommentResponse` DTOs.

## 3. Using `@CrossOrigin` on every controller
**AI suggestion:** Add `@CrossOrigin(origins = "*")` on each controller class.
**Why wrong:** Overly permissive CORS (`*`) is a security risk; duplicated config across controllers.
**Fix:** Centralized CORS in `WebConfig` with configurable allowed origins from `application-dev.yml`.

## 4. Frontend: catching errors without parsing API body
**AI suggestion:** Show generic "Something went wrong" on any fetch failure.
**Why wrong:** Acceptance criteria require meaningful errors in the UI; backend returns `message` and `fieldErrors`.
**Fix:** `handleResponse()` in `lib/api.ts` parses error JSON and surfaces field-level errors.

## 5. Skipping invalid transition tests
**AI suggestion:** Only test the happy path OPEN → IN_PROGRESS → RESOLVED → CLOSED.
**Why wrong:** State machine enforcement is a core requirement; invalid transitions must return 409.
**Fix:** Added parameterized `TicketStatusMachineTest` and integration test for CLOSED → OPEN rejection.
