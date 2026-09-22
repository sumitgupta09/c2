package com.support.tickets.dto;

import com.support.tickets.domain.TicketPriority;
import com.support.tickets.domain.TicketStatus;
import jakarta.validation.constraints.Size;

public record UpdateTicketRequest(
        @Size(max = 200) String title,
        @Size(max = 5000) String description,
        TicketPriority priority,
        @Size(max = 100) String assignee,
        TicketStatus status
) {
}
