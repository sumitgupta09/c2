# Reusable Prompt Templates

## 1. Spec-scoped implementation (chaining step N)

```
Read @.constitution.md and @spec/[FILE].md.

Implement ONLY: [single task from docs/implementation-plan.md phase X.Y]

Constraints:
- Do not modify unrelated files
- Add/update tests per @commands/generate-tests.md
- Run self-critique per @commands/self-critique.md when done

Output: list of files changed and test command to run.
```

## 2. Structured review output

```
Review the git diff against @commands/review-code.md.

Return JSON:
{
  "critical": ["..."],
  "warnings": ["..."],
  "suggestions": ["..."],
  "tests_missing": ["..."],
  "pass": true|false
}
```

## 3. Compression-friendly context block

When attaching large context, use this header:

```
## Context (compressed)
- Goal: [one line]
- Spec: @spec/[file].md sections [X-Y]
- Layer: [controller|service|test|ui]
- Out of scope: [explicit list]
```

## 4. Cache / memory seed (for MCP or session notes)

```
Persist these project facts for later prompts:
- Invalid status transition → HTTP 409
- Runtime DB: H2 file (dev), PostgreSQL (prod via docker-compose)
- Public: create ticket + get by ID without login
- Staff: JWT login, dashboard at /dashboard
```
