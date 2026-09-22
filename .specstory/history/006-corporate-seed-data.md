# 006 — Corporate Support Desk Seed Data

**Date:** 2026-09-10  
**Stage:** Implementation / Data  
**Inputs:** domain enums for teams, roles, and types

---

## Role
Platform engineer preparing demo data for a corporate support desk.

## Goal
Seed realistic users and tickets so local demos look like a real company support organization.

## Seed design
- Teams: Support Desk, IT, Database, HR, Finance, Accounts (or equivalent)  
- Hierarchy: main admin above teams; each team has a lead (`TEAM_ADMIN`) and agents  
- Sample tickets spanning ticket types (technical, database, HR, billing, account, other)  
- Demo passwords only in seeder and README; never in production config files  

## Constraints
- Prefer idempotent seeding (do not blindly duplicate users on every restart)  
- Use password hashing (BCrypt)  
- Do not commit real personal data or production secrets  

## Definition of Done
- [ ] App starts with usable demo accounts  
- [ ] Sample tickets visible for staff dashboard demos  
- [ ] README lists demo logins  
- [ ] Seeder logs a clear “seeded” message once  

## Output format
Team/user matrix, sample ticket titles, README snippet for demos.
