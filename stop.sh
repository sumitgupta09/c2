#!/usr/bin/env bash
# Gupta Corp Support Desk — stop backend + frontend locally.
#
# Usage (from project root):
#   ./stop.sh

set -euo pipefail

ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

echo "=========================================="
echo "  Gupta Corp Support Desk — Local Stop"
echo "=========================================="

stop_pid_file() {
  local label="$1"
  local pid_file="$2"

  if [[ ! -f "$pid_file" ]]; then
    return
  fi

  local pid
  pid="$(cat "$pid_file" 2>/dev/null || true)"
  if [[ -n "$pid" ]] && kill -0 "$pid" 2>/dev/null; then
    echo "Stopping $label (pid $pid) ..."
    kill "$pid" 2>/dev/null || true
    sleep 1
    kill -9 "$pid" 2>/dev/null || true
  fi
  rm -f "$pid_file"
}

echo ""
echo "[1/2] Stopping tracked processes ..."
stop_pid_file "frontend" "$ROOT/.frontend.pid"
stop_pid_file "backend"  "$ROOT/.backend.pid"

echo "[2/2] Freeing ports 8080, 3000, 3001, 3002 ..."
for port in 8080 3000 3001 3002; do
  if command -v fuser >/dev/null 2>&1; then
    fuser -k "${port}/tcp" 2>/dev/null || true
  elif command -v lsof >/dev/null 2>&1; then
    pids="$(lsof -t -i:"${port}" 2>/dev/null || true)"
    if [[ -n "$pids" ]]; then
      kill $pids 2>/dev/null || true
    fi
  fi
done

echo ""
echo "=========================================="
echo "  System is DOWN"
echo "=========================================="
echo "  Start again: ./start.sh"
echo "=========================================="
