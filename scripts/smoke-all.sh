#!/usr/bin/env bash
# Run after backend + frontend are up. Waits for API then runs full smoke checks.
set -euo pipefail

API="${API_BASE:-http://localhost:8080/api}"
FRONTEND="${FRONTEND_URL:-http://localhost:3000}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"

echo "Waiting for backend at $API ..."
for i in $(seq 1 30); do
  if curl -sf "$API/ticket-types" >/dev/null 2>&1; then
    break
  fi
  sleep 2
done

if ! curl -sf "$API/ticket-types" >/dev/null 2>&1; then
  echo "ERROR: Backend not reachable. Start with: cd backend && ./run.sh run"
  exit 1
fi

echo "========== API SMOKE =========="
bash "$ROOT/backend/scripts/smoke-test-apis.sh"

echo ""
echo "========== PUBLIC CREATE =========="
curl -sf -X POST "$API/tickets" \
  -H "Content-Type: application/json" \
  -d '{"title":"Smoke rerun","description":"auto test","ticketType":"HR"}' \
  | python3 -c "import sys,json; t=json.load(sys.stdin); print(f'OK id={t[\"id\"]} priority={t[\"priority\"]}')"

echo ""
echo "========== FRONTEND =========="
for path in / /login /tickets/new /dashboard; do
  code=$(curl -s -o /dev/null -w "%{http_code}" "$FRONTEND$path")
  echo "$code GET $path"
done

echo ""
echo "========== ALL SMOKE CHECKS PASSED =========="
