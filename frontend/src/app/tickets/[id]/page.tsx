"use client";

import Link from "next/link";
import { useParams } from "next/navigation";
import { FormEvent, useCallback, useEffect, useState } from "react";
import { addComment, getTicket, listAgents, STATUS_TRANSITIONS, updateTicket, type AgentUser } from "@/lib/api";
import { AuthSession, getSession, isAdmin, isTeamAdmin } from "@/lib/auth";
import type { ApiError, Ticket, TicketPriority, TicketStatus } from "@/lib/types";

const PRIORITIES: TicketPriority[] = ["LOW", "MEDIUM", "HIGH", "URGENT"];

function TicketDetailForm() {
  const params = useParams();
  const id = Number(params.id);

  const [session, setSession] = useState<AuthSession | null>(null);
  const [agents, setAgents] = useState<AgentUser[]>([]);
  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<TicketPriority>("MEDIUM");
  const [assignee, setAssignee] = useState("");
  const [status, setStatus] = useState<TicketStatus>("OPEN");
  const [commentBody, setCommentBody] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const admin = isAdmin(session);
  const teamAdmin = isTeamAdmin(session);
  const canReassign = admin || teamAdmin;
  const canEdit = admin || teamAdmin || (session && ticket?.assignee.toLowerCase() === session.email.toLowerCase());

  useEffect(() => {
    setSession(getSession());
    const onChange = () => setSession(getSession());
    window.addEventListener("auth-changed", onChange);
    return () => window.removeEventListener("auth-changed", onChange);
  }, []);

  useEffect(() => {
    if (canReassign) {
      listAgents().then(setAgents).catch(() => {});
    }
  }, [canReassign]);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getTicket(id);
      setTicket(data);
      setTitle(data.title);
      setDescription(data.description);
      setPriority(data.priority);
      setAssignee(data.assignee);
      setStatus(data.status);
    } catch (e) {
      const err = e as { message?: string };
      setError(err.message ?? "Failed to load ticket");
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    if (!Number.isNaN(id)) load();
  }, [id, load]);

  const allowedStatuses = ticket ? [ticket.status, ...STATUS_TRANSITIONS[ticket.status]] : [];

  async function handleUpdate(e: FormEvent) {
    e.preventDefault();
    if (!canEdit) return;
    setSaving(true);
    setError(null);
    setFieldErrors({});
    try {
      const payload: Record<string, unknown> = { title, description, status };
      if (canReassign) {
        payload.assignee = assignee;
      }
      if (admin) {
        payload.priority = priority;
      }
      const updated = await updateTicket(id, payload);
      setTicket(updated);
      setStatus(updated.status);
    } catch (err) {
      const apiErr = err as ApiError;
      setError(apiErr.message ?? "Failed to update");
      setFieldErrors(apiErr.fieldErrors ?? {});
    } finally {
      setSaving(false);
    }
  }

  async function handleAddComment(e: FormEvent) {
    e.preventDefault();
    if (!canEdit) return;
    setError(null);
    setFieldErrors({});
    try {
      const updated = await addComment(id, { body: commentBody });
      setTicket(updated);
      setCommentBody("");
    } catch (err) {
      const apiErr = err as ApiError;
      setError(apiErr.message ?? "Failed to add comment");
      setFieldErrors(apiErr.fieldErrors ?? {});
    }
  }

  if (loading) return <div className="card empty-state">Loading ticket...</div>;
  if (!ticket) return <div className="alert">{error ?? "Ticket not found"}</div>;

  return (
    <>
      <header className="page-header">
        <div>
          <h1>Ticket #{ticket.id}</h1>
          <p className="page-subtitle">{ticket.title}</p>
        </div>
        <Link href="/" className="btn">Back</Link>
      </header>

      {error && <div className="alert">{error}</div>}

      {!session && (
        <div className="alert alert-info">
          Status view only. Staff sign in to update and resolve this ticket.
        </div>
      )}
      {session && !canEdit && (
        <div className="alert alert-info">
          View-only — you are not the assignee for this ticket.
        </div>
      )}

      <div className="detail-grid">
        <div className="stat-box">
          <label>Status</label>
          <p><span className={`badge badge-${ticket.status.toLowerCase()}`}>{ticket.status.replace("_", " ")}</span></p>
        </div>
        <div className="stat-box">
          <label>Type</label>
          <p><span className="badge badge-type">{ticket.ticketType}</span></p>
        </div>
        <div className="stat-box">
          <label>Priority</label>
          <p>{ticket.priority}</p>
        </div>
        <div className="stat-box">
          <label>Assignee</label>
          <p>{ticket.assignee}</p>
        </div>
        <div className="stat-box">
          <label>Created by</label>
          <p>{ticket.createdBy}</p>
        </div>
      </div>

      <form className={`card ${!canEdit ? "readonly" : ""}`} onSubmit={handleUpdate}>
        <div className="form-group">
          <label htmlFor="title">Title</label>
          <input id="title" value={title} onChange={(e) => setTitle(e.target.value)} disabled={!canEdit} />
          {fieldErrors.title && <div className="field-error">{fieldErrors.title}</div>}
        </div>
        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea id="description" value={description} onChange={(e) => setDescription(e.target.value)} disabled={!canEdit} />
          {fieldErrors.description && <div className="field-error">{fieldErrors.description}</div>}
        </div>
        {admin && (
          <div className="form-group">
            <label htmlFor="priority">Priority</label>
            <select id="priority" value={priority} onChange={(e) => setPriority(e.target.value as TicketPriority)}>
              {PRIORITIES.map((p) => <option key={p} value={p}>{p}</option>)}
            </select>
            <p className="meta">Admin can override auto-assigned priority</p>
          </div>
        )}
        {canReassign && (
          <div className="form-group">
            <label htmlFor="assignee">Reassign to</label>
            <select id="assignee" value={assignee} onChange={(e) => setAssignee(e.target.value)}>
              {agents.map((a) => (
                <option key={a.id} value={a.email}>{a.name} — {a.team} ({a.email})</option>
              ))}
              {!agents.find((a) => a.email === assignee) && assignee && (
                <option value={assignee}>{assignee}</option>
              )}
            </select>
            <p className="meta">
              {admin ? "Support desk manager can assign across all teams" : "Assign within your team queue"}
            </p>
          </div>
        )}
        <div className="form-group">
          <label htmlFor="status">Status</label>
          <select id="status" value={status} onChange={(e) => setStatus(e.target.value as TicketStatus)} disabled={!canEdit}>
            {allowedStatuses.map((s) => <option key={s} value={s}>{s.replace("_", " ")}</option>)}
          </select>
        </div>
        {canEdit && (
          <button type="submit" className="btn btn-primary" disabled={saving}>
            {saving ? "Saving..." : "Save Changes"}
          </button>
        )}
      </form>

      <section className="comments card" style={{ marginTop: "1rem" }}>
        <h2 style={{ marginTop: 0 }}>Comments</h2>
        {ticket.comments.length === 0 ? (
          <p className="meta">No comments yet.</p>
        ) : (
          ticket.comments.map((c) => (
            <div key={c.id} className="comment">
              <strong>{c.author}</strong>
              <span className="meta"> · {new Date(c.createdAt).toLocaleString()}</span>
              <p style={{ margin: "0.5rem 0 0" }}>{c.body}</p>
            </div>
          ))
        )}
        {canEdit && (
          <form onSubmit={handleAddComment} style={{ marginTop: "1rem" }}>
            <div className="form-group">
              <label htmlFor="body">Add comment</label>
              <textarea id="body" value={commentBody} onChange={(e) => setCommentBody(e.target.value)} required />
              {fieldErrors.body && <div className="field-error">{fieldErrors.body}</div>}
            </div>
            <button type="submit" className="btn btn-primary">Post Comment</button>
          </form>
        )}
      </section>
    </>
  );
}

export default function TicketDetailPage() {
  return <TicketDetailForm />;
}
