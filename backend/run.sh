#!/usr/bin/env bash
set -euo pipefail

# Spring Boot 3.x needs Java 17+. Always prefer sdkman Java 21 (ignore shell JAVA_HOME).
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
