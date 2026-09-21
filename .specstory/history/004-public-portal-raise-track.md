# 004 — Public Portal: Raise Request & Track by ID

**Date:** 2026-09-10  
**Stage:** Implementation / UI  
**Inputs:** `@spec/ui-flow.md` `@spec/api-contract.md`

---

## Role
You are designing a consumer-style Help Center experience on top of the existing ticket engine.

## Goal
Anyone can raise a support request and track it by ticket ID without logging in. Only authenticated staff can resolve or change status.

## Product rules
1. Public create form requires ticket type; system auto-assigns to the correct team lead  
2. Type `OTHER` assigns to main admin; admin can reassign  
3. On create, show/return a ticket ID for later tracking  
4. Public track page by ID (read + comments UX as designed)  
5. Staff console remains behind login (`/dashboard`)  
6. UI must feel modern and trustworthy (not a dated admin CRUD skin)

## Hard constraints
- Do not remove backend state machine or validation  
- Do not require login for create or track-by-id  
- Keep API error envelope intact for UI display  
- Update `spec/ui-flow.md` if routes change (spec before or with the change)

## Definition of Done
- [ ] Public `/` → create + track CTAs  
- [ ] `/tickets/new` works without auth  
- [ ] `/tickets/[id]` tracks status by ID  
- [ ] Staff-only resolve path still JWT-protected  
- [ ] Smoke check of public + staff happy paths  

## Output format
Route map, auth boundaries, files changed, smoke results.
