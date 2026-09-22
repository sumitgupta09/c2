"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";

export default function HomePage() {
  const router = useRouter();
  const [trackId, setTrackId] = useState("");
  const [error, setError] = useState<string | null>(null);

  function handleTrack(e: FormEvent) {
    e.preventDefault();
    const id = trackId.trim();
    if (id && /^\d+$/.test(id)) {
      router.push(`/tickets/${id}`);
    } else {
      setError("Enter a valid ticket ID number");
    }
  }

  return (
    <div className="portal-hero">
      <header className="portal-header">
        <div className="portal-badge">✦ Enterprise Support Platform 2026</div>
        <h1>Gupta Corp Employee Portal</h1>
        <p className="page-subtitle">
          Raise HR, IT, finance, database, or account requests. Track status in real time — auto-routed to the right team.
        </p>
      </header>

      <div className="portal-stats">
        <div className="stat-pill">
          <strong>6</strong>
          <span>Request Types</span>
        </div>
        <div className="stat-pill">
          <strong>24/7</strong>
          <span>Track by ID</span>
        </div>
        <div className="stat-pill">
          <strong>Auto</strong>
          <span>Team Routing</span>
        </div>
      </div>

      {error && <div className="alert">{error}</div>}

      <div className="portal-grid">
        <div className="card portal-card card-3d">
          <div className="portal-card-icon">🎫</div>
          <h2>Raise a Request</h2>
          <p className="meta">
            Submit a support ticket. Auto-assigned to IT, HR, Finance, Database, or Accounts based on type.
          </p>
          <Link href="/tickets/new" className="btn btn-primary btn-glow" style={{ marginTop: "1.25rem" }}>
            Create Ticket →
          </Link>
        </div>

        <div className="card portal-card card-3d">
          <div className="portal-card-icon">🔍</div>
          <h2>Check Ticket Status</h2>
          <p className="meta">Enter the ticket ID from your confirmation — no login required.</p>
          <form className="track-box" onSubmit={handleTrack} style={{ marginTop: "1.25rem" }}>
            <input
              type="text"
              placeholder="Ticket ID e.g. 42"
              value={trackId}
              onChange={(e) => setTrackId(e.target.value)}
              aria-label="Ticket ID"
            />
            <button type="submit" className="btn btn-primary">Track</button>
          </form>
        </div>

        <div className="card portal-card card-3d">
          <div className="portal-card-icon">🔐</div>
          <h2>Staff Login</h2>
          <p className="meta">
            Agents and admins sign in to triage queues, update status, assign, and resolve tickets.
          </p>
          <Link href="/login" className="btn" style={{ marginTop: "1.25rem" }}>
            Staff Sign In
          </Link>
        </div>
      </div>
    </div>
  );
}
