# 009 — Close Partially Completed Items

**Date:** 2026-09-10  
**Stage:** Fix  
**Inputs:** previous audit PARTIALLY DONE list

---

## Role
You finish incomplete compliance work with the same rigor as core features.

## Goal
Convert every PARTIALLY DONE item into DONE with durable artefacts.

## Typical partials to close
- PostgreSQL verification script/evidence  
- SpecStory / prompt history completeness  
- `.specify/tasks.md`  
- PR evidence documentation  
- Any weak test or missing traceability row  

## Hard constraints
- No fake screenshots or fabricated prompt history  
- Prefer scripts and markdown evidence that a reviewer can re-run  
- Keep changes minimal and reviewable  

## Definition of Done
- [ ] Previous PARTIALLY DONE list is empty or explicitly justified  
- [ ] New artefacts linked from README or deliverable checklist  
- [ ] Tests still pass  

## Output format
Before/after status table; files added; verification commands.
