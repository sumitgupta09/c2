#!/usr/bin/env bash
set -euo pipefail

export JAVA_HOME="${JAVA_HOME:-$HOME/.sdkman/candidates/java/21.0.1-amzn}"
export PATH="$JAVA_HOME/bin:$PATH"

if command -v mvn >/dev/null 2>&1; then
  MVN=mvn
elif [[ -x "$HOME/.sdkman/candidates/maven/current/bin/mvn" ]]; then
  MVN="$HOME/.sdkman/candidates/maven/current/bin/mvn"
else
  echo "Maven not found. Install via: sdk install maven"
  exit 1
fi

case "${1:-run}" in
  test)  "$MVN" test ;;
  smoke) bash "$(dirname "$0")/scripts/smoke-test-apis.sh" ;;
  run)   "$MVN" spring-boot:run ;;
  *)     echo "Usage: $0 [run|test|smoke]"; exit 1 ;;
esac
