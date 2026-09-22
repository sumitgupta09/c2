# UI Flow Specification

**Framework:** Next.js 14 (App Router)  
**API base:** `NEXT_PUBLIC_API_URL` (default `http://localhost:8080/api`)  
**Version:** 1.1 (aligned with public portal + staff JWT console)

---

## Screen Map

```
/                 Public Help / portal (brand landing)
├── Raise request → /tickets/new
└── Track by ID   → /tickets/[id]

/tickets/new      Public create (ticket type, title, description, optional reporter email)
└── Submit → redirect to /tickets/[id]

/tickets/[id]     Public track + comments; staff edit when logged in
├── Edit fields + Save (staff)
├── Status change — valid options only (staff; backend enforces)
└── Comments

/login            Staff JWT login
/dashboard        Staff list: search + status filter + stats (auth required)
```

---

## Page: `/` — Public portal

| Element | Behaviour |
|---------|-----------|
| Brand / CTAs | Raise a request, Track ticket, Staff login |
| Not a full staff ticket table | Staff listing lives on `/dashboard` |

---

## Page: `/dashboard` — Staff ticket list (auth)

| Element | Behaviour |
|---------|-----------|
| Search input | Debounced ~300ms → `GET /tickets?keyword={value}` |
| Status dropdown | `GET /tickets?status={value}` or all if empty |
| Table columns | ID, Title, Status, Priority, Type, Assignee, Updated |
| Row click | Navigate to `/tickets/{id}` |
| Empty state | "No tickets found." |
| Error state | Red alert with API `message` |
| Unauthenticated | Redirect to `/login` |

---

## Page: `/tickets/new` — Create Ticket (public)

| Field | Type | Required |
|-------|------|----------|
| Ticket type / category | select | Yes |
| Title | text | Yes |
| Description | textarea | Yes |
| Reporter email | email | No |
| Priority | — | Auto from type (not on form) |
| Assignee | — | Auto from type (not on form) |

**On submit:** `POST /tickets` → redirect to detail on 201.  
**On 400:** Show `fieldErrors` under each field + `message` banner.

---

## Page: `/login` — Staff login

| Field | Required |
|-------|----------|
| Email | Yes |
| Password | Yes |

**On success:** store JWT session → `/dashboard`.  
**On failure:** show API `message`.

---

## Page: `/tickets/[id]` — Ticket Detail

### Display / Edit
| Field | Public | Staff (when authorized) |
|-------|--------|-------------------------|
| Title / description | Read | Edit |
| Priority | Read | Admin override |
| Assignee | Read | Team lead / admin reassign |
| Status | Read | Dropdown: current + `STATUS_TRANSITIONS` |
| Ticket type | Read | Read |
| Created / Updated | Read | Read |

**Save:** `PATCH /tickets/{id}` with changed fields (JWT).

### Status Change UX
- Dropdown from `STATUS_TRANSITIONS[currentStatus]` + current status (`lib/statusTransitions.ts`)
- On **409**: show backend `message` in alert (e.g. invalid transition); do not update displayed status
- Backend `TicketStatusMachine` is authoritative — UI never the sole enforcer

### Comments Section
- List comments (author, timestamp, body)
- Form: body (author from session or guest name as implemented) → `POST /tickets/{id}/comments`
- On success: refresh list

---

## Error Display Rules

| API Response | UI Treatment |
|--------------|--------------|
| 400 + fieldErrors | Red text under each invalid field |
| 400/409/404 + message | Red banner at top of page |
| 401 | Session cleared; prompt re-login |
| Network error | "Cannot reach backend…" (or equivalent) |

**API client** (`lib/api.ts` + `parseApiError`) must parse error JSON for components.

---

## Loading States

- List / dashboard: loading while fetching
- Detail: loading until ticket loaded
- Submit buttons: disabled + Saving / Creating during request

---

## Non-Requirements (UI)

- No pagination controls
- No dark mode toggle
- No toast library required (banner sufficient)
