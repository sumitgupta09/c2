# 012 — Modern Glassmorphism Portal UI

**Date:** 2026-09-10  
**Stage:** Frontend design upgrade  
**Inputs:** existing Next.js routes; keep flows intact

---

## Role
You are a product-minded frontend engineer upgrading visual quality without breaking workflows.

## Goal
Deliver a modern glassmorphism Support Desk UI that feels contemporary and branded (Gupta Corp), while preserving create/track/login/dashboard behaviour.

## Design direction
- Glass cards, subtle depth, animated atmospheric background (orbs/grid)  
- Strong brand presence on first viewport  
- Clear hierarchy: portal CTAs, staff login, dashboard stats  
- Meaningful loading / empty / error states  
- Avoid generic “AI purple gradient on white” cliché; pick a coherent branded palette  

## Hard constraints
- Do not break API client error parsing  
- Do not move state machine enforcement into UI-only logic  
- Keep accessibility basics (labels, contrast for errors)  
- Smoke-test critical routes after CSS/layout changes  

## Definition of Done
- [ ] `/`, `/login`, `/dashboard`, ticket pages updated  
- [ ] Visual system centralized (e.g. `globals.css` variables)  
- [ ] Smoke routes return success  
- [ ] No new secrets or unsafe dependencies  

## Output format
Before/after notes, files touched, smoke results, any intentional visual tradeoffs.
