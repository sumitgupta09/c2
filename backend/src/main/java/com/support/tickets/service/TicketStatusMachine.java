package com.support.tickets.service;

import com.support.tickets.domain.TicketStatus;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Component
public class TicketStatusMachine {

    private static final Map<TicketStatus, Set<TicketStatus>> TRANSITIONS = Map.of(
            TicketStatus.OPEN, EnumSet.of(TicketStatus.IN_PROGRESS, TicketStatus.CANCELLED),
            TicketStatus.IN_PROGRESS, EnumSet.of(TicketStatus.RESOLVED, TicketStatus.CANCELLED),
            TicketStatus.RESOLVED, EnumSet.of(TicketStatus.CLOSED),
            TicketStatus.CLOSED, EnumSet.noneOf(TicketStatus.class),
            TicketStatus.CANCELLED, EnumSet.noneOf(TicketStatus.class)
    );

    public boolean canTransition(TicketStatus from, TicketStatus to) {
        if (from == to) {
            return true;
        }
        return TRANSITIONS.getOrDefault(from, EnumSet.noneOf(TicketStatus.class)).contains(to);
    }

    public Set<TicketStatus> allowedTransitions(TicketStatus from) {
        return TRANSITIONS.getOrDefault(from, EnumSet.noneOf(TicketStatus.class));
    }
}
