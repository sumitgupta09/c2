#!/usr/bin/env bash
# Verify PostgreSQL runtime profile (NFR-02 / PR-01).
# Requires: Docker, Java 21, Maven
set -euo pipefail

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

echo "Starting PostgreSQL..."
docker compose up -d --wait

export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:postgresql://localhost:5432/tickets
export DATABASE_USERNAME=tickets
export DATABASE_PASSWORD=tickets
export JWT_SECRET=postgres-verify-secret-32chars-min

echo "Starting backend on :8080 (prod profile)..."
cd backend
export JAVA_HOME="${JAVA_HOME:-$HOME/.sdkman/candidates/java/21.0.1-amzn}"
export PATH="$JAVA_HOME/bin:$PATH"

# Use ddl-auto=update for first run against empty Postgres
SPRING_JPA_HIBERNATE_DDL_AUTO=update mvn -q spring-boot:run &
PID=$!
trap 'kill $PID 2>/dev/null || true' EXIT

for i in $(seq 1 60); do
  if curl -sf http://localhost:8080/api/ticket-types >/dev/null 2>&1; then
    break
  fi
  sleep 2
done

echo "Creating ticket via API..."
ID=$(curl -sf -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{"title":"Postgres verify","description":"runtime persistence","ticketType":"HR"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['id'])")

echo "Stopping backend (simulating restart)..."
kill $PID 2>/dev/null || true
wait $PID 2>/dev/null || true
sleep 3

echo "Restarting backend..."
SPRING_JPA_HIBERNATE_DDL_AUTO=update mvn -q spring-boot:run &
PID=$!

for i in $(seq 1 60); do
  if curl -sf "http://localhost:8080/api/tickets/$ID" >/dev/null 2>&1; then
    break
  fi
  sleep 2
done

TITLE=$(curl -sf "http://localhost:8080/api/tickets/$ID" \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['title'])")

if [[ "$TITLE" == "Postgres verify" ]]; then
  echo "OK: Ticket $ID survived PostgreSQL restart"
  kill $PID 2>/dev/null || true
  exit 0
else
  echo "FAIL: Expected 'Postgres verify', got '$TITLE'"
  kill $PID 2>/dev/null || true
  exit 1
fi
