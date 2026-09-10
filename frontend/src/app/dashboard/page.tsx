"use client";

import Link from "next/link";
import { useCallback, useEffect, useState } from "react";
import RequireAuth from "@/components/RequireAuth";
import { listTickets } from "@/lib/api";
import { AuthSession, getSession, isAdmin, isTeamAdmin, roleLabel } from "@/lib/auth";
import type { Ticket, TicketStatus } from "@/lib/types";

const STATUSES: Array<TicketStatus | ""> = ["", "OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED", "CANCELLED"];

function statusBadgeClass(status: TicketStatus): string {
  return `badge badge-${status.toLowerCase()}`;
}

function StaffDashboard() {
  const [session, setSession] = useState<AuthSession | null>(null);
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [keyword, setKeyword] = useState("");
  const [status, setStatus] = useState<TicketStatus | "">("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setSession(getSession());
    const onChange = () => setSession(getSession());
    window.addEventListener("auth-changed", onChange);
    return () => window.removeEventListener("auth-changed", onChange);
  }, []);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await listTickets({ keyword, status });
      setTickets(data);
    } catch (e) {
      const err = e as { message?: string };
      setError(err.message ?? "Failed to load tickets");
    } finally {
      setLoading(false);
    }
  }, [keyword, status]);

  useEffect(() => {
    const timer = setTimeout(load, 300);
    return () => clearTimeout(timer);
  }, [load]);

  return (
    <>
      <header className="page-header">
        <div>
          <h1>
            {isAdmin(session) ? "Support Desk — All Queues" : isTeamAdmin(session) ? "Team Queue" : "My Assigned Tickets"}
          </h1>
          <p className="page-subtitle">
            {isAdmin(session)
              ? "Corporate support desk manager view across IT, HR, Finance, Database & Accounts"
              : isTeamAdmin(session)
              ? `${roleLabel(session)} — triage and assign within your team`
              : "Tickets assigned to you — update status and add comments"}
          </p>
        </div>
      </header>

      {error && <div className="alert">{error}</div>}

      {!loading && tickets.length > 0 && (
        <div className="dashboard-stats">
          <div className="dash-stat">
            <div className="dash-stat-value">{tickets.length}</div>
            <div className="dash-stat-label">In Queue</div>
          </div>
          <div className="dash-stat">
            <div className="dash-stat-value">{tickets.filter((t) => t.status === "OPEN").length}</div>
            <div className="dash-stat-label">Open</div>
          </div>
          <div className="dash-stat">
            <div className="dash-stat-value">{tickets.filter((t) => t.status === "IN_PROGRESS").length}</div>
            <div className="dash-stat-label">In Progress</div>
          </div>
          <div className="dash-stat">
            <div className="dash-stat-value">{tickets.filter((t) => ["RESOLVED", "CLOSED"].includes(t.status)).length}</div>
            <div className="dash-stat-label">Resolved</div>
          </div>
        </div>
      )}

      <div className="filters">
        <input
          type="search"
          placeholder="Search title, description, or ID..."
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <select value={status} onChange={(e) => setStatus(e.target.value as TicketStatus | "")}>
          <option value="">All statuses</option>
          {STATUSES.filter(Boolean).map((s) => (
            <option key={s} value={s}>{s.replace("_", " ")}</option>
          ))}
        </select>
      </div>

      {loading ? (
        <div className="shimmer" />
      ) : tickets.length === 0 ? (
        <div className="card empty-state">
          <p>No tickets found for your queue.</p>
        </div>
      ) : (
        <div className="ticket-grid">
          {tickets.map((ticket) => (
            <Link key={ticket.id} href={`/tickets/${ticket.id}`} className="ticket-card">
              <div className="ticket-id">#{ticket.id}</div>
              <div>
                <h3>{ticket.title}</h3>
                <div className="ticket-card-meta">
                  <span className={statusBadgeClass(ticket.status)}>
                    {ticket.status.replace("_", " ")}
                  </span>
                  <span className="badge badge-type">{ticket.ticketType}</span>
                  <span className="meta">{ticket.assignee}</span>
                </div>
              </div>
              <div className="meta" style={{ textAlign: "right" }}>
                {new Date(ticket.updatedAt).toLocaleDateString()}
              </div>
            </Link>
          ))}
        </div>
      )}
    </>
  );
}

export default function DashboardPage() {
  return (
    <RequireAuth>
      <StaffDashboard />
    </RequireAuth>
  );
}
