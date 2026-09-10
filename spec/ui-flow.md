# UI Flow Specification

**Framework:** Next.js 14 (App Router)  
**API base:** `NEXT_PUBLIC_API_URL` (default `http://localhost:8080/api`)

---

## Screen Map

```
/  (Ticket List)
├── Search + Status Filter
├── [New Ticket] → /tickets/new
└── Click row → /tickets/[id]

/tickets/new  (Create)
└── Submit → redirect to /tickets/[id]

/tickets/[id]  (Detail)
├── Edit fields + Save
├── Status change (valid options only)
└── Comments list + Add comment
```

---

## Page: `/` — Ticket List

| Element | Behaviour |
|---------|-----------|
| Search input | Debounced 300ms → `GET /tickets?keyword={value}` |
| Status dropdown | `GET /tickets?status={value}` or all if empty |
| Table columns | ID, Title, Status, Priority, Assignee, Updated |
| New Ticket button | Navigate to `/tickets/new` |
| Row click | Navigate to `/tickets/{id}` |
| Empty state | "No tickets found." |
| Error state | Red alert with API `message` |

---

## Page: `/tickets/new` — Create Ticket

| Field | Type | Required |
|-------|------|----------|
| Title | text | Yes |
| Description | textarea | Yes |
| Priority | select (LOW–URGENT) | Yes |
| Assignee | text | No |

**On submit:** `POST /tickets` → redirect to detail on 201.  
**On 400:** Show `fieldErrors` under each field + `message` banner.

---

## Page: `/tickets/[id]` — Ticket Detail

### Display / Edit
| Field | Editable |
|-------|----------|
| Title | Yes |
| Description | Yes |
| Priority | Yes |
| Assignee | Yes |
| Status | Yes (dropdown: current + allowed next) |
| Created / Updated | Read-only |

**Save:** `PATCH /tickets/{id}` with changed fields.

### Status Change UX
- Dropdown populated from `STATUS_TRANSITIONS[currentStatus]` + current status
- On 409: show `message` in alert; do not update displayed status

### Comments Section
- List all comments (author, timestamp, body)
- Form: author + body → `POST /tickets/{id}/comments`
- On success: refresh comment list, clear body field

---

## Error Display Rules

| API Response | UI Treatment |
|--------------|--------------|
| 400 + fieldErrors | Red text under each invalid field |
| 400/409/404 + message | Red banner at top of page |
| Network error | "Unable to reach server. Is the backend running?" |

**API client** (`lib/api.ts`) must parse error JSON and throw structured object for components.

---

## Loading States

- List page: "Loading..." while fetching
- Detail page: "Loading..." until ticket loaded
- Submit buttons: disabled + "Saving..." / "Creating..." during request

---

## Non-Requirements (UI)

- No login screen
- No pagination controls
- No dark mode toggle
- No toast notifications (banner sufficient)
