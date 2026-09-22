# 005 — Auto Priority From Ticket Type

**Date:** 2026-09-10  
**Stage:** Implementation  
**Inputs:** `@spec/requirements.md` assignment service

---

## Role
Backend engineer simplifying the customer create flow.

## Goal
Derive priority automatically from ticket type. Customers never pick priority. Admins may override later.

## Rules
- Remove priority from the public create form  
- Map each ticket type to a default priority in one service method (single source of truth)  
- Admin override remains available on update  
- Keep validation and API contract consistent; update specs if needed  

## Constraints
- No duplicated priority mapping in controller and frontend  
- Do not break auto-assign behaviour  
- Add or adjust tests for default priority by type  

## Definition of Done
- [ ] Create request DTO/UI has no customer priority field  
- [ ] Created tickets receive the correct default priority  
- [ ] Admin can still change priority  
- [ ] Tests cover at least two type→priority mappings  

## Output format
Priority mapping table, files changed, tests run.
