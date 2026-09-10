"use client";

import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { login } from "@/lib/auth";

export default function LoginPage() {
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await login(email, password);
      router.push("/dashboard");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Login failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="login-page">
      <div style={{ textAlign: "center", marginBottom: "0.5rem" }}>
        <div className="topbar-logo" style={{ margin: "0 auto 1rem", width: 56, height: 56, fontSize: "1.5rem" }}>G</div>
      </div>
      <h1>Gupta Corp</h1>
      <p className="meta" style={{ textAlign: "center" }}>
        Corporate Support Desk — secure staff access
      </p>

      {error && <div className="alert">{error}</div>}

      <form className="card card-3d login-card" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="email">Work Email</label>
          <input
            id="email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            placeholder="you@guptacorp.com"
          />
        </div>
        <div className="form-group">
          <label htmlFor="password">Password</label>
          <input
            id="password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="btn btn-primary btn-glow" style={{ width: "100%" }} disabled={loading}>
          {loading ? "Signing in..." : "Sign In"}
        </button>
      </form>

      <p className="meta" style={{ textAlign: "center", marginTop: "1rem" }}>
        <a href="/">← Back to employee portal</a>
      </p>

      <div className="card login-hints" style={{ marginTop: "1rem" }}>
        <p><strong>Demo corporate accounts</strong></p>
        <p>Main Admin: <code>support.admin@guptacorp.com</code> / <code>admin123</code></p>
        <p>IT Team Lead: <code>it.lead@guptacorp.com</code> / <code>lead123</code></p>
        <p>IT Agent: <code>it.agent@guptacorp.com</code> / <code>agent123</code></p>
        <p>HR Team Lead: <code>hr.lead@guptacorp.com</code> / <code>lead123</code></p>
        <p>DB Agent: <code>db.agent@guptacorp.com</code> / <code>agent123</code></p>
      </div>
    </div>
  );
}
