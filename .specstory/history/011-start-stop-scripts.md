# 011 — Start/Stop Local Dev Scripts

**Date:** 2026-09-10  
**Stage:** Operations  
**Inputs:** backend `run.sh`, frontend `npm run dev`

---

## Role
You are improving developer experience without changing product behaviour.

## Goal
Provide reliable one-command local start and stop for backend + frontend.

## Requirements
- `start.sh`: free ports 8080/3000/(optional 3001/3002), start backend then frontend, wait for health, print URLs and demo login  
- `stop.sh`: stop tracked PIDs and free ports  
- Force **Java 21** from sdkman (ignore wrong shell `JAVA_HOME`)  
- Fail fast with log tails if backend does not become healthy  
- Document usage in README  

## Hard constraints
- No secrets in scripts  
- Prefer `fuser`/`lsof` port cleanup that is safe for local dev  
- Do not require Docker for the default H2-file dev path  

## Definition of Done
- [ ] `./start.sh` brings API + UI up  
- [ ] `./stop.sh` takes them down  
- [ ] Wrong Java version cannot silently hang “Waiting for API…”  

## Output format
Script behaviour summary, README snippet, example successful start output.
