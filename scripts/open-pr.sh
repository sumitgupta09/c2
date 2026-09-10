#!/usr/bin/env bash
# Open a PR after pushing cursor/compliance-gaps (requires gh CLI + remote).
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

BRANCH="${1:-cursor/compliance-gaps}"

if ! git remote get-url origin >/dev/null 2>&1; then
  echo "No git remote. Add one first:"
  echo "  git remote add origin <url>"
  echo "  git push -u origin main"
  echo "  git push -u origin $BRANCH"
  exit 1
fi

if ! command -v gh >/dev/null 2>&1; then
  echo "gh CLI not found. Install: https://cli.github.com/"
  exit 1
fi

git push -u origin "$BRANCH"

gh pr create --base main --head "$BRANCH" \
  --title "Support Desk: spec-driven implementation + compliance artifacts" \
  --body "$(cat <<'EOF'
## Summary
- Full Support Ticket Management System (Spring Boot 3 + Next.js 14)
- Spec-first: constitution, 7 spec files, traceability matrix, implementation plan
- MCP server with 3 tools; prompt engineering templates
- 29 automated backend tests; PostgreSQL restart verified

## Test plan
- [x] `cd backend && mvn test` (29 tests)
- [x] `./scripts/verify-postgres.sh` (PostgreSQL persistence)
- [ ] `./scripts/smoke-all.sh` (backend + frontend running)
- [x] `cd frontend && npm run build`

## Traceability
See `docs/requirements-traceability.md`
EOF
)"
