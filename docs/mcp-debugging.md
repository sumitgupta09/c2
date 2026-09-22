# MCP Tool-Call Debugging

## Setup

```bash
cd mcp-server && npm install
```

Ensure backend is running on port 8080. Cursor reads `.cursor/mcp.json` automatically.

## Tools (3)

| Tool | API | Auth |
|------|-----|------|
| `get_ticket` | `GET /api/tickets/{id}` | Public |
| `list_ticket_types` | `GET /api/ticket-types` | Public |
| `create_ticket` | `POST /api/tickets` | Public |

## Manual debug (stdio)

```bash
cd mcp-server
DEBUG=mcp:* npm start
```

In another terminal, test API directly:

```bash
curl -s http://localhost:8080/api/ticket-types | head
curl -s http://localhost:8080/api/tickets/1
```

## Common failures

| Symptom | Cause | Fix |
|---------|-------|-----|
| `Connection refused` | Backend down | `cd backend && ./run.sh run` |
| `Unknown tool` | Typo in tool name | Use exact names from `list_ticket_types` |
| MCP not listed in Cursor | Config path | Reload window; check `.cursor/mcp.json` |
| `ticketId is required` | Missing arg | Pass `{ "ticketId": 1 }` |

## Verify in Cursor

1. Open MCP panel → `supportdesk` server should show 3 tools.
2. Ask: "Use get_ticket to check ticket 1"
3. Check MCP logs for `[mcp] tool_call get_ticket`
