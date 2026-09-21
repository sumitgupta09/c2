#!/usr/bin/env bash
# Gupta Corp Support Desk — one command to start everything locally.
#
# Usage (from project root):
#   ./start.sh
#
# Then open:
#   Frontend: http://localhost:3000
#   API:      http://localhost:8080/api
#
# Optional — run smoke test after servers are up:
#   ./scripts/smoke-all.sh
#
# Stop servers:
#   ./stop.sh

set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

# Spring Boot 3.x needs Java 17+. Force sdkman Java 21 (shell JAVA_HOME may be Java 8).
if [[ -x "$HOME/.sdkman/candidates/java/21.0.1-amzn/bin/java" ]]; then
  export JAVA_HOME="$HOME/.sdkman/candidates/java/21.0.1-amzn"
else
  JAVA21="$(ls -d "$HOME"/.sdkman/candidates/java/21.* 2>/dev/null | head -1)"
  if [[ -n "$JAVA21" && -x "$JAVA21/bin/java" ]]; then
    export JAVA_HOME="$JAVA21"
  else
    echo "ERROR: Java 21 not found. Install: sdk install java 21.0.1-amzn"
    exit 1
  fi
fi
export PATH="$JAVA_HOME/bin:$PATH"

echo "=========================================="
echo "  Gupta Corp Support Desk — Local Start"
echo "=========================================="

# ── 1. Kill old processes on backend/frontend ports ──
echo ""
echo "[1/3] Freeing ports 8080, 3000, 3001, 3002 ..."
for port in 8080 3000 3001 3002; do
  if command -v fuser >/dev/null 2>&1; then
    fuser -k "${port}/tcp" 2>/dev/null || true
  elif command -v lsof >/dev/null 2>&1; then
    kill $(lsof -t -i:"${port}") 2>/dev/null || true
  fi
done
sleep 2

# ── 2. Start backend (port 8080) ──
echo "[2/3] Starting backend on :8080 ..."
cd "$ROOT/backend"
: > "$ROOT/backend.log"
nohup env JAVA_HOME="$JAVA_HOME" PATH="$PATH" ./run.sh run > "$ROOT/backend.log" 2>&1 &
backend_pid=$!
echo "$backend_pid" > "$ROOT/.backend.pid"

echo "      Waiting for API (Java: $($JAVA_HOME/bin/java -version 2>&1 | head -1))..."
for i in $(seq 1 45); do
  if curl -sf http://localhost:8080/api/ticket-types >/dev/null 2>&1; then
    echo "      Backend ready."
    break
  fi
  if ! kill -0 "$backend_pid" 2>/dev/null || grep -q "BUILD FAILURE" "$ROOT/backend.log" 2>/dev/null; then
    echo "ERROR: Backend failed to start."
    tail -15 "$ROOT/backend.log"
    exit 1
  fi
  if [[ $i -eq 45 ]]; then
    echo "ERROR: Backend timed out. Check: tail -f $ROOT/backend.log"
    exit 1
  fi
  sleep 2
done

# ── 3. Start frontend (port 3000) ──
echo "[3/3] Starting frontend on :3000 ..."
cd "$ROOT/frontend"
nohup npm run dev > "$ROOT/frontend.log" 2>&1 &
echo $! > "$ROOT/.frontend.pid"

echo "      Waiting for UI..."
for i in $(seq 1 30); do
  if curl -sf http://localhost:3000/ >/dev/null 2>&1; then
    echo "      Frontend ready."
    break
  fi
  if [[ $i -eq 30 ]]; then
    echo "WARN: Frontend slow to start. Check: tail -f $ROOT/frontend.log"
    break
  fi
  sleep 2
done

echo ""
echo "=========================================="
echo "  System is UP"
echo "=========================================="
echo "  Portal:    http://localhost:3000"
echo "  Login:     http://localhost:3000/login"
echo "  API:       http://localhost:8080/api"
echo ""
echo "  Staff login: support.admin@guptacorp.com / admin123"
echo ""
echo "  Logs:  tail -f backend.log frontend.log"
echo "  Smoke: ./scripts/smoke-all.sh"
echo "  Stop:  ./stop.sh"
echo "=========================================="
