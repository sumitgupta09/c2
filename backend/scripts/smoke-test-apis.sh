#!/usr/bin/env bash
set -euo pipefail

BASE="${API_BASE:-http://localhost:8080/api}"
EMAIL="${TEST_EMAIL:-support.admin@guptacorp.com}"
PASSWORD="${TEST_PASSWORD:-admin123}"

echo "=== 1. POST /auth/login ==="
LOGIN=$(curl -sf -X POST "$BASE/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")
TOKEN=$(echo "$LOGIN" | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")
echo "OK — token received"

AUTH="Authorization: Bearer $TOKEN"

echo "=== 2. GET /ticket-types ==="
curl -sf "$BASE/ticket-types" -H "$AUTH" | python3 -m json.tool
echo "OK"

echo "=== 3. POST /tickets (auto-assign) ==="
TICKET=$(curl -sf -X POST "$BASE/tickets" -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"title":"Smoke test","description":"API check","ticketType":"TECHNICAL"}')
ID=$(echo "$TICKET" | python3 -c "import sys,json; print(json.load(sys.stdin)['id'])")
echo "OK — ticket id=$ID"

echo "=== 4. GET /tickets/$ID ==="
curl -sf "$BASE/tickets/$ID" -H "$AUTH" | python3 -m json.tool
echo "OK"

echo ""
echo "All API checks passed. Ticket ID for UAT: $ID"
