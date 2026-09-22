# 008 — Full Requirements Audit → Fix → Re-Verify Loop

**Date:** 2026-09-10  
**Stage:** Testing → Review → Fix (repeat until green)  
**Inputs:** acceptance criteria, `docs/requirements-traceability.md`, test suite

---

## Role
Engineer responsible for meeting every practical requirement.

## Goal
Audit every requirement against real code and tests. Fix gaps. Re-run verification. Repeat until complete.

## Method
1. Inspect repository evidence for each requirement  
2. Mark DONE / PARTIALLY DONE / NOT DONE with proof  
3. If any gap exists: implement the smallest fix  
4. Run relevant tests (`mvn test`, smoke, persistence verify)  
5. Re-audit the same requirement  
6. Continue until all practical items are DONE  

## Constraints
- Evidence over file existence alone  
- Do not claim “complete” without commands and results  
- Do not rebuild from scratch  
- Keep secrets out of git  

## Definition of Done
- [ ] Backend tests green  
- [ ] State machine invalid transitions covered  
- [ ] Persistence restart verified  
- [ ] Final checklist with evidence for each acceptance criterion  

## Output format
Final checklist (criterion → evidence → status). Include exact commands run and outcomes.
