# 007 — Compliance Artefacts: Spec-Kit, Prompts, MCP

**Date:** 2026-09-10  
**Stage:** Review / Compliance  
**Inputs:** existing `spec/`, `rules/`, `docs/`

---

## Role
Engineer closing assessment compliance gaps with file-level evidence.

## Goal
Confirm the repository shows Spec-Driven Development, steering files, prompt history, MCP usage, and traceability.

## Verify and complete (DONE / PARTIALLY DONE / NOT DONE + evidence)
1. Spec-first workflow artefacts: constitution, specs, tasks, implementation, review commands, PR evidence, traceability  
2. Prompt artefacts: templates, few-shot examples, self-critique command, prompt history  
3. MCP: minimal server with 2–3 tools, Cursor config, and debugging notes  
4. Testing evidence: state machine unit, API integration, and persistence restart  

## Constraints
- Do not give credit for empty directories  
- Do not rewrite working product features unless a gap requires it  
- Prefer adding missing docs and tests over cosmetic README claims  
- Record genuine AI mistakes when found (`docs/ai-review.md` or notes)  

## Definition of Done
- [ ] Each compliance area has file-level evidence  
- [ ] MCP tools are callable with the backend running (documented)  
- [ ] Traceability matrix maps Requirement → Spec → Code → Test  
- [ ] Prompt history exists under `.specstory/history/` and `docs/prompt-history.md`  

## Output format
Table of items with status and evidence paths; list of files created or updated; remaining blockers only if truly blocked.
