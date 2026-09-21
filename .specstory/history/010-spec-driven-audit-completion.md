# 010 — Spec-Driven Audit & Completion

**Date:** 2026-09-21  
**Stage:** Inspect → Spec update → Incremental fix → Test → Review → Report  

---

## Role
You are the lead engineer and AI engineering assistant for this repository.

## Goal
Audit the existing project against Spec-Driven Development requirements and complete missing pieces. **Do not rebuild the application from scratch.**

## Mandatory workflow
Requirement → Specification → Plan / Tasks → Implementation → Testing → Review → Fix

## Execution rules
1. Inspect the entire repository first  
2. Produce a gap analysis (completed / partial / missing / incorrect / risky)  
3. Update specifications **before** changing behaviour when specs drift  
4. Break work into small tasks; implement one logical task at a time  
5. Run tests after meaningful changes  
6. Critically review AI output; document genuine mistakes  
7. Write a final status report with acceptance-criteria evidence  

## Must verify
- rules / skills / commands exist and are substantive  
- seven specs exist and match the running system  
- `.specstory/history/` + `docs/prompt-history.md`  
- backend state machine + 409 + tests  
- persistence, validation, UI errors  
- no committed secrets  
- `docs/ai-review.md` with real AI mistakes  
- `docs/implementation-status.md` final report  

## Hard constraints
- Do not delete working functionality to regenerate it  
- Do not invent AI mistakes  
- Prefer smallest fixes that restore spec↔code alignment  

## Definition of Done
- [ ] Gap analysis written  
- [ ] Spec drift resolved  
- [ ] Tests green (backend + any new FE unit tests)  
- [ ] AI review + implementation-status docs exist  
- [ ] Acceptance criteria matrix has evidence  

## Output format
Start with gap analysis. End with `docs/implementation-status.md` summary and score of remaining risks only.
