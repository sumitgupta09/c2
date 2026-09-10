"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { AuthSession, getSession, isAdmin, isTeamAdmin, logout, roleLabel } from "@/lib/auth";

export default function AuthBar() {
  const router = useRouter();
  const [session, setSession] = useState<AuthSession | null>(null);

  useEffect(() => {
    setSession(getSession());
    const onChange = () => setSession(getSession());
    window.addEventListener("auth-changed", onChange);
    return () => window.removeEventListener("auth-changed", onChange);
  }, []);

  function handleLogout() {
    logout();
    router.push("/");
  }

  function badgeClass(): string {
    if (isAdmin(session)) return "badge-admin";
    if (isTeamAdmin(session)) return "badge-type";
    return "badge-agent";
  }

  return (
    <nav className="topbar">
      <div>
        <Link href="/" className="topbar-brand-link">
          <div className="topbar-brand">Acme Corp Support Desk</div>
        </Link>
        {session && (
          <div className="topbar-user">
            {session.name}
            <span className={`badge ${badgeClass()}`}>{roleLabel(session)}</span>
          </div>
        )}
      </div>
      <div className="topbar-actions">
        <Link href="/tickets/new" className="btn btn-primary">Raise Request</Link>
        {session ? (
          <>
            <Link href="/dashboard" className="btn">Dashboard</Link>
            <button type="button" className="btn" onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <Link href="/login" className="btn">Staff Login</Link>
        )}
      </div>
    </nav>
  );
}
