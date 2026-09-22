import { getSession } from "./auth";
import { parseApiError } from "./parseApiError";
import type { ApiError, SupportTeam, Ticket, TicketPriority, TicketStatus, TicketType, UserRole } from "./types";

export { STATUS_TRANSITIONS } from "./statusTransitions";
export { parseApiError } from "./parseApiError";

const API_BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api";

function buildHeaders(): HeadersInit {
  const headers: HeadersInit = { "Content-Type": "application/json" };
  const session = getSession();
  if (session?.token) {
    headers["Authorization"] = `Bearer ${session.token}`;
  }
  return headers;
}

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    let error: ApiError = {
      message: response.status === 401
        ? "Session expired. Please log in again."
        : "Request failed",
      fieldErrors: {},
    };
    try {
      const body = await response.json();
      error = parseApiError(body as Record<string, unknown>, response.status);
    } catch {
      // keep default
    }
    if (response.status === 401) {
      const { clearSession } = await import("./auth");
      clearSession();
    }
    throw error;
  }
  return response.json() as Promise<T>;
}

async function apiFetch(url: string, options: RequestInit = {}): Promise<Response> {
  try {
    return await fetch(url, {
      ...options,
      headers: { ...buildHeaders(), ...options.headers },
    });
  } catch {
    throw { message: "Cannot reach backend. Is the API running on http://localhost:8080?", fieldErrors: {} } as ApiError;
  }
}

export interface AgentUser {
  id: number;
  email: string;
  name: string;
  role: UserRole;
  team: SupportTeam;
}

export interface TicketTypeOption {
  type: TicketType;
  label: string;
  assigneeHint: string;
  priorityHint: string;
}

export async function listTicketTypes(): Promise<TicketTypeOption[]> {
  const response = await apiFetch(`${API_BASE}/ticket-types`);
  return handleResponse<TicketTypeOption[]>(response);
}

export async function listAgents(): Promise<AgentUser[]> {
  const response = await apiFetch(`${API_BASE}/users/agents`);
  return handleResponse<AgentUser[]>(response);
}

export async function listTickets(params?: {
  keyword?: string;
  status?: TicketStatus | "";
  assignee?: string;
}): Promise<Ticket[]> {
  const search = new URLSearchParams();
  if (params?.keyword) search.set("keyword", params.keyword);
  if (params?.status) search.set("status", params.status);
  if (params?.assignee) search.set("assignee", params.assignee);
  const query = search.toString();
  const response = await apiFetch(`${API_BASE}/tickets${query ? `?${query}` : ""}`);
  return handleResponse<Ticket[]>(response);
}

export async function getTicket(id: number): Promise<Ticket> {
  const response = await apiFetch(`${API_BASE}/tickets/${id}`);
  return handleResponse<Ticket>(response);
}

export async function createTicket(data: {
  title: string;
  description: string;
  ticketType: TicketType;
  reporterEmail?: string;
}): Promise<Ticket> {
  const response = await apiFetch(`${API_BASE}/tickets`, {
    method: "POST",
    body: JSON.stringify(data),
  });
  return handleResponse<Ticket>(response);
}

export async function updateTicket(
  id: number,
  data: Partial<{
    title: string;
    description: string;
    priority: TicketPriority;
    assignee: string;
    status: TicketStatus;
  }>
): Promise<Ticket> {
  const response = await apiFetch(`${API_BASE}/tickets/${id}`, {
    method: "PATCH",
    body: JSON.stringify(data),
  });
  return handleResponse<Ticket>(response);
}

export async function addComment(
  ticketId: number,
  data: { body: string }
): Promise<Ticket> {
  const response = await apiFetch(`${API_BASE}/tickets/${ticketId}/comments`, {
    method: "POST",
    body: JSON.stringify(data),
  });
  return handleResponse<Ticket>(response);
}

