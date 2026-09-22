#!/usr/bin/env bash
# Fail if likely secrets are tracked by git. Safe patterns only — no secret values printed.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

echo "========== SECRETS SCAN =========="
fail=0

if git ls-files | grep -E '(^|/)\.env$|\.pem$|id_rsa$|credentials\.json$|serviceAccount' >/dev/null; then
  echo "FAIL: sensitive filename tracked by git"
  git ls-files | grep -E '(^|/)\.env$|\.pem$|id_rsa$|credentials\.json$|serviceAccount' || true
  fail=1
else
  echo "PASS: no .env / pem / private-key filenames tracked"
fi

# High-risk hardcoded secret patterns in tracked source (exclude docs, seeder passwords noted as demo)
hits=$(git grep -nE 'AKIA[0-9A-Z]{16}|BEGIN (RSA |OPENSSH )?PRIVATE KEY|api[_-]?key\s*=\s*['\''\"][a-zA-Z0-9]{20,}' \
  -- ':!*.md' ':!docs/**' ':!.specstory/**' ':!prompts/**' 2>/dev/null || true)
if [[ -n "$hits" ]]; then
  echo "FAIL: possible hardcoded secrets in source"
  echo "$hits"
  fail=1
else
  echo "PASS: no high-risk secret patterns in application source"
fi

if git check-ignore -q .env 2>/dev/null || grep -q '^\.env' .gitignore 2>/dev/null; then
  echo "PASS: .env is gitignored"
else
  echo "FAIL: .env not gitignored"
  fail=1
fi

test -f .env.example && echo "PASS: .env.example present" || { echo "FAIL: missing .env.example"; fail=1; }

echo ""
[[ "$fail" -eq 0 ]] && echo "SECRETS SCAN PASSED" || { echo "SECRETS SCAN FAILED"; exit 1; }
