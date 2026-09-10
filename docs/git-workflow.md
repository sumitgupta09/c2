# Git Workflow & PR Evidence

## Branch

- **Feature branch:** `cursor/compliance-gaps`
- **Base:** `main` (no commits on main yet — branch contains full project history)

## Commits (3)

```
3bbd345 Add spec-driven planning artifacts and documentation.
addd3d8 Implement support desk application with compliance tooling.
3e53676 Fix persistence integration test for context restart.
```

## PR checklist (ready to open)

When a remote is configured:

```bash
git remote add origin <your-github-url>
git push -u origin cursor/compliance-gaps
gh pr create --title "Support Desk: spec-driven implementation + compliance artifacts" --body "$(cat <<'EOF'
## Summary
- Full Support Ticket Management System (Spring Boot + Next.js)
- Spec-first artifacts: constitution, 7 spec files, traceability matrix
- MCP server with 3 tools, prompt engineering templates
- 28 automated backend tests including persistence restart test

## Test plan
- [ ] `cd backend && mvn test`
- [ ] `./scripts/smoke-all.sh` (backend + frontend running)
- [ ] `docker compose up -d` + `SPRING_PROFILES_ACTIVE=prod` smoke
EOF
)"
```

## Traceability

Requirement → spec → implementation → test mapping: `docs/requirements-traceability.md`

## Review artifacts used

- `commands/review-code.md`
- `commands/review-spec.md`
- `commands/self-critique.md`
- `docs/ai-review-notes.md`
