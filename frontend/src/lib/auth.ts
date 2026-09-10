import type { SupportTeam, UserRole } from "./types";

export interface AuthSession {
  token: string;
  email: string;
  name: string;
  role: UserRole;
  team?: SupportTeam;
}

const STORAGE_KEY = "support-tickets-auth";

const API_BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api";

export function getSession(): AuthSession | null {
  if (typeof window === "undefined") return null;
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return null;
  try {
    const session = JSON.parse(raw) as AuthSession;
    if (!session.token || !session.email || !session.role) {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
    return session;
  } catch {
    localStorage.removeItem(STORAGE_KEY);
    return null;
  }
}

export function saveSession(session: AuthSession): void {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
}

export function clearSession(): void {
  localStorage.removeItem(STORAGE_KEY);
  window.dispatchEvent(new Event("auth-changed"));
}

export async function login(email: string, password: string): Promise<AuthSession> {
  let response: Response;
  try {
    response = await fetch(`${API_BASE}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });
  } catch {
    throw new Error("Cannot reach backend. Is the API running on http://localhost:8080?");
  }
  if (!response.ok) {
    const err = await response.json().catch(() => ({ message: "Login failed" }));
    throw new Error(err.message ?? "Login failed");
  }
  const data = await response.json();
  const session: AuthSession = {
    token: data.token,
    email: data.email,
    name: data.name,
    role: data.role,
    team: data.team,
  };
  saveSession(session);
  window.dispatchEvent(new Event("auth-changed"));
  return session;
}

export function logout(): void {
  clearSession();
}

export function isAdmin(session: AuthSession | null): boolean {
  return session?.role === "ADMIN";
}

export function isTeamAdmin(session: AuthSession | null): boolean {
  return session?.role === "TEAM_ADMIN";
}

export function isAgent(session: AuthSession | null): boolean {
  return session?.role === "AGENT";
}

export function roleLabel(session: AuthSession | null): string {
  if (!session) return "";
  if (isAdmin(session)) return "Support Desk Manager";
  if (isTeamAdmin(session)) return "Team Lead";
  return "Agent";
}
