# 003 — Staff Auth, Roles, and Assignment Rules

**Date:** 2026-09-10  
**Stage:** Implementation  
**Inputs:** `@spec/architecture.md` `@rules/java-springboot.md` existing ticket services

---

## Role
You are extending a working ticket system into a realistic staff support desk without breaking public create/track flows.

## Goal
Add JWT staff authentication and authorization so ticket changes behave like production support software.

## Requirements
- Roles: `ADMIN`, `TEAM_ADMIN`, `AGENT`
- Staff login via JWT
- Only authorized roles may update status/fields/assignee per team rules
- Preserve existing state machine enforcement
- Seed demo users for local development (passwords only in seeder/docs, never as production secrets)

## Hard constraints
- Do not disable security “for convenience”
- Do not put authorization only in the frontend
- Keep error responses consistent with API standards
- Minimal change set: prefer extending current services over a rewrite

## Definition of Done
- [ ] Login endpoint works and returns JWT  
- [ ] Protected staff mutations require auth  
- [ ] Unauthorized updates return 401/403 appropriately  
- [ ] Seeded demo accounts documented in README  
- [ ] Existing ticket tests still pass or are updated intentionally  

## Output format
Auth model summary, endpoints, seed accounts (emails only + demo passwords in README), test results.
