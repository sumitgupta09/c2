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
        <h1>Gupta Corp Employee Portal</h1>
        <p className="page-subtitle">
          Corporate office support — raise HR, IT, finance, database, or account requests. Track by ticket ID anytime.
        </p>
      </header>

      {error && <div className="alert">{error}</div>}

      <div className="portal-grid">
        <div className="card portal-card">
          <h2>Raise a Request</h2>
          <p className="meta">
            Submit a support ticket. It is auto-assigned to the right team based on type.
          </p>
          <Link href="/tickets/new" className="btn btn-primary" style={{ marginTop: "1rem" }}>
            Create Ticket
          </Link>
        </div>

        <div className="card portal-card">
          <h2>Check Ticket Status</h2>
          <p className="meta">Enter the ticket ID you received when you created your request.</p>
          <form className="track-box" onSubmit={handleTrack} style={{ marginTop: "1rem" }}>
            <input
              type="text"
              placeholder="Ticket ID e.g. 42"
              value={trackId}
              onChange={(e) => setTrackId(e.target.value)}
              aria-label="Ticket ID"
            />
            <button type="submit" className="btn btn-primary">Check Status</button>
          </form>
        </div>

        <div className="card portal-card">
          <h2>Staff Login</h2>
          <p className="meta">
            Support agents and admins sign in here to update status, assign, and resolve tickets.
          </p>
          <Link href="/login" className="btn" style={{ marginTop: "1rem" }}>
            Staff Sign In
          </Link>
        </div>
      </div>
    </div>
  );
}
