import type { ApiError } from "./types";

/** Parse backend error JSON into UI-friendly ApiError. Exported for unit tests. */
export function parseApiError(body: Record<string, unknown>, status: number): ApiError {
  const message =
    (typeof body.message === "string" && body.message) ||
    (typeof body.error === "string" && body.error) ||
    (status === 401 ? "Session expired. Please log in again." : "Request failed");
  const fieldErrors =
    body.fieldErrors && typeof body.fieldErrors === "object"
      ? (body.fieldErrors as Record<string, string>)
      : {};
  return { message, fieldErrors };
}
