package com.support.tickets.dto;

import com.support.tickets.domain.Ticket;
import com.support.tickets.domain.TicketPriority;
import com.support.tickets.domain.TicketStatus;
import com.support.tickets.domain.TicketType;

import java.time.Instant;
import java.util.List;

public record TicketResponse(
        Long id,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        TicketType ticketType,
        String assignee,
        String createdBy,
        Instant createdAt,
        Instant updatedAt,
        List<CommentResponse> comments
) {
    public static TicketResponse from(Ticket ticket) {
        List<CommentResponse> comments = ticket.getComments().stream()
                .map(CommentResponse::from)
                .toList();
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getTicketType(),
                ticket.getAssignee(),
                ticket.getCreatedBy(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                comments
        );
    }
}
