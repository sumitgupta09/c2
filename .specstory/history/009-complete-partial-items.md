# 009 — Close Partially Completed Items

**Date:** 2026-09-10  
**Stage:** Fix  
**Inputs:** PARTIALLY DONE list from the requirements audit

---

## Role
Engineer finishing incomplete compliance work.

## Goal
Convert every PARTIALLY DONE item into DONE with durable artefacts.

## Typical partials to close
- PostgreSQL verification script or evidence  
- SpecStory / prompt history completeness  
- `.specify/tasks.md`  
- PR evidence documentation  
- Any weak test or missing traceability row  

## Constraints
- No fake screenshots or fabricated prompt history  
- Prefer scripts and markdown evidence that a reviewer can re-run  
- Keep changes minimal and reviewable  

## Definition of Done
- [ ] PARTIALLY DONE list is empty or explicitly justified  
- [ ] New artefacts linked from README or deliverable checklist  
- [ ] Tests still pass  

## Output format
Before/after status table; files added; verification commands.
