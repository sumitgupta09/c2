# Command: Self-Critique

Run after AI generates code and before marking a task complete.

---

## Prompt template (copy to AI)

```
You just implemented: [TASK DESCRIPTION]

Critique your own work against:
@.constitution.md
@rules/java-springboot.md
@spec/[relevant-spec].md

Respond in this structure:

## Correct
- [what matches spec and rules]

## Violations
- [constitution/rule/spec breaches — or "none"]

## Missing tests
- [untested behaviour — or "none"]

## Security concerns
- [issues — or "none"]

## Recommended fixes (priority order)
1. ...
```

---

## Human checklist

- [ ] Critique output reviewed
- [ ] Violations fixed before commit
- [ ] Findings appended to `docs/ai-review-notes.md` if AI made a mistake
- [ ] `mvn test` still passes

---

## When to run

- After each implementation-plan phase
- Before opening a PR
- After large AI-generated diffs
