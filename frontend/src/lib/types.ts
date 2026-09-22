export type TicketStatus =
  | "OPEN"
  | "IN_PROGRESS"
  | "RESOLVED"
  | "CLOSED"
  | "CANCELLED";

export type TicketPriority = "LOW" | "MEDIUM" | "HIGH" | "URGENT";

export type TicketType = "TECHNICAL" | "DATABASE" | "HR" | "BILLING" | "ACCOUNT" | "OTHER";

export type UserRole = "ADMIN" | "TEAM_ADMIN" | "AGENT";

export type SupportTeam =
  | "SUPPORT_DESK"
  | "IT"
  | "DATABASE"
  | "HR"
  | "FINANCE"
  | "ACCOUNTS";

export interface Comment {
  id: number;
  author: string;
  body: string;
  createdAt: string;
}

export interface Ticket {
  id: number;
  title: string;
  description: string;
  status: TicketStatus;
  priority: TicketPriority;
  ticketType: TicketType;
  assignee: string;
  createdBy: string;
  createdAt: string;
  updatedAt: string;
  comments: Comment[];
}

export interface ApiError {
  message: string;
  fieldErrors: Record<string, string>;
}
