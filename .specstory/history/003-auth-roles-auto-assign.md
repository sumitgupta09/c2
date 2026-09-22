# 003 — Staff Auth, Roles, and Assignment Rules

**Date:** 2026-09-10  
**Stage:** Implementation  
**Inputs:** `@spec/architecture.md` `@rules/java-springboot.md` existing ticket services

---

## Role
Engineer extending the ticket system into a staff support desk.

## Goal
Add JWT staff authentication and authorization so ticket changes follow production-style access control, without breaking public create and track flows.

## Requirements
- Roles: `ADMIN`, `TEAM_ADMIN`, `AGENT`
- Staff login via JWT
- Only authorized roles may update status, fields, or assignee per team rules
- Preserve existing state machine enforcement
- Seed demo users for local development (passwords only in seeder/docs, never as production secrets)

## Constraints
- Do not disable security for convenience
- Do not put authorization only in the frontend
- Keep error responses consistent with API standards
- Prefer extending current services over a rewrite

## Definition of Done
- [ ] Login endpoint works and returns JWT  
- [ ] Protected staff mutations require auth  
- [ ] Unauthorized updates return 401/403 appropriately  
- [ ] Seeded demo accounts documented in README  
- [ ] Existing ticket tests still pass or are updated intentionally  

## Output format
Auth model summary, endpoints, seed accounts (emails only; demo passwords in README), test results.
