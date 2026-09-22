package com.support.tickets.controller;

import com.support.tickets.domain.TicketType;
import com.support.tickets.service.TicketAssignmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ticket-types")
public class TicketTypeController {

    private final TicketAssignmentService assignmentService;

    public TicketTypeController(TicketAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public List<Map<String, String>> listTypes() {
        return Arrays.stream(TicketType.values())
                .map(type -> Map.of(
                        "type", type.name(),
                        "label", formatLabel(type),
                        "assigneeHint", assignmentService.describeAssignment(type),
                        "priorityHint", assignmentService.describePriority(type)
                ))
                .toList();
    }

    private String formatLabel(TicketType type) {
        return switch (type) {
            case TECHNICAL -> "IT / Technical Issue";
            case DATABASE -> "Database / Data Issue";
            case HR -> "HR & People";
            case BILLING -> "Finance & Billing";
            case ACCOUNT -> "Accounts & Access";
            case OTHER -> "Other (Support Desk triage)";
        };
    }
}
