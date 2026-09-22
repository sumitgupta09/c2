#!/usr/bin/env bash
# End-to-end acceptance checks (API + frontend routes).
# Requires: ./start.sh  (or backend + frontend already up)
set -euo pipefail

API="${API_BASE:-http://localhost:8080/api}"
FRONTEND="${FRONTEND_URL:-http://localhost:3000}"
ROOT="$(cd "$(dirname "$0")/.." && pwd)"

pass=0
fail=0
ok() { echo "  PASS  $1"; pass=$((pass + 1)); }
bad() { echo "  FAIL  $1"; fail=$((fail + 1)); }

echo "Waiting for backend..."
for i in $(seq 1 30); do
  curl -sf "$API/ticket-types" >/dev/null 2>&1 && break
  sleep 2
done
curl -sf "$API/ticket-types" >/dev/null 2>&1 || { echo "Backend not up. Run ./start.sh"; exit 1; }

echo ""
echo "========== E2E ACCEPTANCE =========="

# Create
CREATE=$(curl -sf -X POST "$API/tickets" \
  -H "Content-Type: application/json" \
  -d '{"title":"E2E acceptance ticket","description":"created by e2e-acceptance.sh","ticketType":"TECHNICAL"}')
TID=$(echo "$CREATE" | python3 -c "import sys,json; print(json.load(sys.stdin)['id'])")
STATUS=$(echo "$CREATE" | python3 -c "import sys,json; print(json.load(sys.stdin)['status'])")
[[ "$STATUS" == "OPEN" ]] && ok "Create ticket id=$TID status=OPEN" || bad "Create ticket"

# Staff login (list/search/filter require JWT)
TOKEN=$(curl -sf -X POST "$API/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"support.admin@guptacorp.com","password":"admin123"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")
[[ -n "$TOKEN" ]] && ok "Staff login JWT" || bad "Staff login JWT"
AUTH=( -H "Authorization: Bearer $TOKEN" )

# List / search / filter (staff)
curl -sf "${AUTH[@]}" "$API/tickets" | python3 -c "import sys,json; d=json.load(sys.stdin); assert isinstance(d,list) and len(d)>=1" \
  && ok "List tickets" || bad "List tickets"
curl -sf "${AUTH[@]}" "$API/tickets?keyword=E2E" | python3 -c "import sys,json; d=json.load(sys.stdin); assert any('E2E' in t.get('title','') for t in d)" \
  && ok "Search by keyword" || bad "Search by keyword"
curl -sf "${AUTH[@]}" "$API/tickets?status=OPEN" | python3 -c "import sys,json; d=json.load(sys.stdin); assert all(t['status']=='OPEN' for t in d)" \
  && ok "Filter by status OPEN" || bad "Filter by status"

# View (public by id)
curl -sf "$API/tickets/$TID" | python3 -c "import sys,json; t=json.load(sys.stdin); assert t['id']==int('$TID')" \
  && ok "View ticket details" || bad "View ticket details"

curl -sf -X PATCH "$API/tickets/$TID" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"E2E acceptance ticket updated"}' >/dev/null \
  && ok "Update title" || bad "Update title"

curl -sf -X POST "$API/tickets/$TID/comments" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"body":"E2E comment"}' >/dev/null \
  && ok "Add comment" || bad "Add comment"

# Valid transition
curl -sf -X PATCH "$API/tickets/$TID" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"status":"IN_PROGRESS"}' \
  | python3 -c "import sys,json; assert json.load(sys.stdin)['status']=='IN_PROGRESS'" \
  && ok "Valid transition OPEN→IN_PROGRESS" || bad "Valid transition"

# Invalid transition → 409
CODE=$(curl -s -o /tmp/e2e409.json -w "%{http_code}" -X PATCH "$API/tickets/$TID" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"status":"CLOSED"}')
[[ "$CODE" == "409" ]] && ok "Invalid transition rejected (409)" || bad "Invalid transition (got HTTP $CODE)"

# Validation → 400
CODE=$(curl -s -o /tmp/e2e400.json -w "%{http_code}" -X POST "$API/tickets" \
  -H "Content-Type: application/json" \
  -d '{"title":"","description":"x","ticketType":"OTHER"}')
[[ "$CODE" == "400" ]] && ok "Backend validation (400)" || bad "Backend validation (got HTTP $CODE)"

# Frontend routes
for path in / /login /tickets/new "/tickets/$TID" /dashboard; do
  code=$(curl -s -o /dev/null -w "%{http_code}" "$FRONTEND$path")
  [[ "$code" == "200" ]] && ok "Frontend GET $path → 200" || bad "Frontend GET $path → $code"
done

echo ""
echo "========== RESULTS: $pass passed, $fail failed =========="
[[ "$fail" -eq 0 ]] || exit 1
echo "ALL E2E ACCEPTANCE CHECKS PASSED"
