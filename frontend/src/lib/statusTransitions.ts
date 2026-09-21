import type { TicketStatus } from "./types";

/**
 * UX helper mirroring backend TicketStatusMachine.
 * Backend remains authoritative; invalid PATCH still returns 409.
 */
export const STATUS_TRANSITIONS: Record<TicketStatus, TicketStatus[]> = {
  OPEN: ["IN_PROGRESS", "CANCELLED"],
  IN_PROGRESS: ["RESOLVED", "CANCELLED"],
  RESOLVED: ["CLOSED"],
  CLOSED: [],
  CANCELLED: [],
};

export function canTransitionUi(from: TicketStatus, to: TicketStatus): boolean {
  if (from === to) return true;
  return STATUS_TRANSITIONS[from].includes(to);
}
