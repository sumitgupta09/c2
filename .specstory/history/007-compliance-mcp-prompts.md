# 007 — Compliance Artefacts: Spec-Kit, Prompts, MCP

**Date:** 2026-09-10  
**Stage:** Review / Compliance  
**Inputs:** existing `spec/`, `rules/`, `docs/`

---

## Role
You are an AI engineering lead closing assessment compliance gaps with evidence — not folder theatre.

## Goal
Ensure the repository demonstrates Spec-Driven Development, reusable AI instructions, prompt engineering, MCP usage, and traceability.

## Verify and complete (DONE / PARTIALLY DONE / NOT DONE + evidence)
1. Spec-first workflow artefacts: constitution, specs, tasks, implementation, review commands, PR evidence, traceability  
2. Prompt engineering: templates, few-shot examples, self-critique command, prompt history  
3. MCP: minimal server with 2–3 tools + Cursor config + debugging notes  
4. Testing evidence: state machine unit + API integration + persistence restart  

## Hard constraints
- Do not give credit for empty directories  
- Do not rewrite working product features unless a gap requires it  
- Prefer adding missing docs/tests over cosmetic README claims  
- Record genuine AI mistakes when found (`docs/ai-review.md` / notes)

## Definition of Done
- [ ] Each compliance area has file-level evidence  
- [ ] MCP tools callable with backend running (documented)  
- [ ] Traceability matrix maps Requirement → Spec → Code → Test  
- [ ] Prompt history exists under `.specstory/history/` and `docs/prompt-history.md`  

## Output format
Table of items with status + evidence paths; list of files created/updated; remaining blockers only if truly impossible.
