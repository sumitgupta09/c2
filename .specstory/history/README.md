# SpecStory History

These are **production-quality prompts** used (and curated) for Spec-Driven Development of the Support Ticket Management System.

Each file is a reusable, high-signal prompt: clear role, constraints, workflow stage, inputs, outputs, and Definition of Done.

**Index:** `docs/prompt-history.md`

| # | File | Workflow stage |
|---|------|----------------|
| 001 | Spec-driven kickoff | Requirement → Spec → Plan |
| 002 | Core ticket build | Implementation → Testing |
| 003 | Auth & authorization | Implementation |
| 004 | Public portal | Implementation / UI |
| 005 | Auto priority | Implementation |
| 006 | Corporate seed data | Implementation |
| 007 | Compliance artefacts | Review / Compliance |
| 008 | Requirements audit loop | Testing → Fix |
| 009 | Close partial gaps | Fix |
| 010 | Spec-driven audit | Review → Fix |
| 011 | Start/stop scripts | Operations |
| 012 | Glassmorphism UI | Frontend |

### How to reuse a prompt
1. Attach only the relevant `@spec/...` and `@rules/...` files.  
2. Copy the **Prompt** section into Cursor.  
3. Do not skip the **Constraints** or **Definition of Done**.
