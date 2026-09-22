# Verification Log

Recorded evidence from automated verification runs.

## Backend tests — 2026-09-10

```
mvn test → 29 tests, 0 failures
  - TicketStatusMachineTest: 12
  - TicketApiIntegrationTest: 15
  - PersistenceIntegrationTest: 2
```

## PostgreSQL runtime — 2026-09-10

```
./scripts/verify-postgres.sh
→ OK: Ticket 1 survived PostgreSQL restart
```

Profile: `prod`  
Database: `docker compose` PostgreSQL on `localhost:5433`  
Config: `application-prod.yml`

## Frontend build — 2026-09-10

```
npm run build → success (Next.js 14)
```

## MCP server — 2026-09-10

```
cd mcp-server && npm install → 95 packages
node index.js → module loads (stdio MCP)
Tools: get_ticket, list_ticket_types, create_ticket
```

## Secrets scan — 2026-09-10

```
git grep password/secret → no hardcoded credentials in tracked source
JWT via ${JWT_SECRET}; DB via env vars in prod profile
```
