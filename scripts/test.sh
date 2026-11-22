#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT="$ROOT/.build"
mkdir -p "$OUT"
find "$ROOT/src/main/java" -name '*.java' -print0 | xargs -0 javac --release 11 -encoding UTF-8 -d "$OUT"
find "$ROOT/src/test/java" -name '*.java' -print0 | xargs -0 javac --release 11 -encoding UTF-8 -cp "$OUT" -d "$OUT"
java -cp "$OUT" datastructures.RegressionSuite
