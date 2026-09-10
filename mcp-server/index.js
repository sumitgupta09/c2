#!/usr/bin/env node
/**
 * Support Desk MCP Server — 3 tools wrapping the REST API.
 * Debug: DEBUG=mcp:* npm start
 */
import { Server } from "@modelcontextprotocol/sdk/server/index.js";
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js";
import {
  CallToolRequestSchema,
  ListToolsRequestSchema,
} from "@modelcontextprotocol/sdk/types.js";

const API_BASE = process.env.API_BASE ?? "http://localhost:8080/api";
const DEBUG = process.env.DEBUG?.includes("mcp");

function log(...args) {
  if (DEBUG) console.error("[mcp]", ...args);
}

async function apiFetch(path, options = {}) {
  const url = `${API_BASE}${path}`;
  log("fetch", options.method ?? "GET", url);
  const res = await fetch(url, {
    ...options,
    headers: { "Content-Type": "application/json", ...options.headers },
  });
  const text = await res.text();
  let body;
  try {
    body = text ? JSON.parse(text) : null;
  } catch {
    body = { raw: text };
  }
  if (!res.ok) {
    const msg = body?.message ?? `HTTP ${res.status}`;
    throw new Error(msg);
  }
  return body;
}

const server = new Server(
  { name: "supportdesk-mcp", version: "1.0.0" },
  { capabilities: { tools: {} } }
);

server.setRequestHandler(ListToolsRequestSchema, async () => ({
  tools: [
    {
      name: "get_ticket",
      description: "Get ticket status and details by ID (public, no login required)",
      inputSchema: {
        type: "object",
        properties: {
          ticketId: { type: "number", description: "Ticket ID number" },
        },
        required: ["ticketId"],
      },
    },
    {
      name: "list_ticket_types",
      description: "List request types with auto-assignment and priority hints",
      inputSchema: { type: "object", properties: {} },
    },
    {
      name: "create_ticket",
      description: "Create a public support request (no login). Returns ticket ID for tracking.",
      inputSchema: {
        type: "object",
        properties: {
          title: { type: "string" },
          description: { type: "string" },
          ticketType: {
            type: "string",
            enum: ["TECHNICAL", "DATABASE", "HR", "BILLING", "ACCOUNT", "OTHER"],
          },
          reporterEmail: { type: "string", description: "Optional contact email" },
        },
        required: ["title", "description", "ticketType"],
      },
    },
  ],
}));

server.setRequestHandler(CallToolRequestSchema, async (request) => {
  const { name, arguments: args } = request.params;
  log("tool_call", name, args);

  try {
    if (name === "get_ticket") {
      const id = args?.ticketId;
      if (!id) throw new Error("ticketId is required");
      const ticket = await apiFetch(`/tickets/${id}`);
      return {
        content: [{ type: "text", text: JSON.stringify(ticket, null, 2) }],
      };
    }

    if (name === "list_ticket_types") {
      const types = await apiFetch("/ticket-types");
      return {
        content: [{ type: "text", text: JSON.stringify(types, null, 2) }],
      };
    }

    if (name === "create_ticket") {
      const { title, description, ticketType, reporterEmail } = args ?? {};
      const ticket = await apiFetch("/tickets", {
        method: "POST",
        body: JSON.stringify({
          title,
          description,
          ticketType,
          reporterEmail: reporterEmail ?? null,
        }),
      });
      return {
        content: [
          {
            type: "text",
            text: JSON.stringify(
              {
                message: "Ticket created. Save this ID to track status.",
                id: ticket.id,
                status: ticket.status,
                priority: ticket.priority,
                assignee: ticket.assignee,
              },
              null,
              2
            ),
          },
        ],
      };
    }

    throw new Error(`Unknown tool: ${name}`);
  } catch (err) {
    log("tool_error", name, err.message);
    return {
      content: [{ type: "text", text: `Error: ${err.message}` }],
      isError: true,
    };
  }
});

const transport = new StdioServerTransport();
await server.connect(transport);
log("server_started", API_BASE);
