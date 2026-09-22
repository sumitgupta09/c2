"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { FormEvent, useEffect, useState } from "react";
import { createTicket, listTicketTypes, type TicketTypeOption } from "@/lib/api";
import type { ApiError, Ticket, TicketType } from "@/lib/types";

export default function NewTicketPage() {
  const router = useRouter();
  const [types, setTypes] = useState<TicketTypeOption[]>([]);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [reporterEmail, setReporterEmail] = useState("");
  const [ticketType, setTicketType] = useState<TicketType>("TECHNICAL");
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [submitting, setSubmitting] = useState(false);
  const [created, setCreated] = useState<Ticket | null>(null);

  useEffect(() => {
    listTicketTypes()
      .then(setTypes)
      .catch(() => setError("Failed to load ticket types"));
  }, []);

  const selectedType = types.find((t) => t.type === ticketType);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setFieldErrors({});
    setSubmitting(true);
    try {
      const ticket = await createTicket({
        title,
        description,
        ticketType,
        reporterEmail: reporterEmail.trim() || undefined,
      });
      setCreated(ticket);
    } catch (err) {
      const apiErr = err as ApiError;
      setError(apiErr.message ?? "Failed to create ticket");
      setFieldErrors(apiErr.fieldErrors ?? {});
    } finally {
      setSubmitting(false);
    }
  }

  if (created) {
    return (
      <div className="card card-3d success-panel">
        <div className="alert alert-success">Request submitted successfully!</div>
        <p className="meta">Save this ticket ID — use it anytime to check your status</p>
        <div className="ticket-id-display">#{created.id}</div>
        <p>
          <span className={`badge badge-${created.status.toLowerCase()}`}>
            {created.status.replace("_", " ")}
          </span>
          <span className="badge badge-type" style={{ marginLeft: "0.5rem" }}>
            {created.ticketType}
          </span>
          <span className="badge" style={{ marginLeft: "0.5rem" }}>
            {created.priority}
          </span>
        </p>
        <p className="meta">Priority was set automatically based on request type.</p>
        <div style={{ display: "flex", gap: "0.5rem", justifyContent: "center", marginTop: "1.5rem", flexWrap: "wrap" }}>
          <Link href={`/tickets/${created.id}`} className="btn btn-primary">Check Status</Link>
          <button type="button" className="btn" onClick={() => router.push("/")}>Back to Portal</button>
        </div>
      </div>
    );
  }

  return (
    <>
      <header className="page-header">
        <div>
          <h1>Raise a Request</h1>
          <p className="page-subtitle">Priority is set automatically — no need to choose</p>
        </div>
        <Link href="/" className="btn">Back</Link>
      </header>

      {error && <div className="alert">{error}</div>}

      <form className="card card-3d" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="ticketType">Request Type</label>
          <select
            id="ticketType"
            value={ticketType}
            onChange={(e) => setTicketType(e.target.value as TicketType)}
          >
            {types.map((t) => (
              <option key={t.type} value={t.type}>{t.label}</option>
            ))}
            {types.length === 0 && (
              <>
                <option value="TECHNICAL">IT / Technical Issue</option>
                <option value="DATABASE">Database / Data Issue</option>
                <option value="HR">HR & People</option>
                <option value="BILLING">Finance & Billing</option>
                <option value="ACCOUNT">Accounts & Access</option>
                <option value="OTHER">Other (Support Desk triage)</option>
              </>
            )}
          </select>
          {selectedType && (
            <div className="type-hint" style={{ marginTop: "0.75rem" }}>
              Routed to: <strong>{selectedType.assigneeHint}</strong>
              <br />
              Auto priority: <strong>{selectedType.priorityHint}</strong>
            </div>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input id="title" value={title} onChange={(e) => setTitle(e.target.value)} required maxLength={200} />
          {fieldErrors.title && <div className="field-error">{fieldErrors.title}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea id="description" value={description} onChange={(e) => setDescription(e.target.value)} required maxLength={5000} />
          {fieldErrors.description && <div className="field-error">{fieldErrors.description}</div>}
        </div>

        <div className="form-group">
          <label htmlFor="reporterEmail">Your email (optional)</label>
          <input
            id="reporterEmail"
            type="email"
            value={reporterEmail}
            onChange={(e) => setReporterEmail(e.target.value)}
            placeholder="you@example.com"
          />
          {fieldErrors.reporterEmail && <div className="field-error">{fieldErrors.reporterEmail}</div>}
        </div>

        <button type="submit" className="btn btn-primary" disabled={submitting}>
          {submitting ? "Submitting..." : "Submit Request"}
        </button>
      </form>
    </>
  );
}
