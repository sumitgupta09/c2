package com.support.tickets.service;

import com.support.tickets.domain.TicketPriority;
import com.support.tickets.domain.TicketType;
import com.support.tickets.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketAssignmentService {

    public static final String MAIN_ADMIN = "support.admin@guptacorp.com";
    public static final String IT_LEAD = "it.lead@guptacorp.com";
    public static final String DB_LEAD = "db.lead@guptacorp.com";
    public static final String HR_LEAD = "hr.lead@guptacorp.com";
    public static final String FINANCE_LEAD = "finance.lead@guptacorp.com";
    public static final String ACCOUNTS_LEAD = "accounts.lead@guptacorp.com";

    private final UserRepository userRepository;

    public TicketAssignmentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String resolveAssignee(TicketType type) {
        String email = switch (type) {
            case TECHNICAL -> IT_LEAD;
            case DATABASE -> DB_LEAD;
            case HR -> HR_LEAD;
            case BILLING -> FINANCE_LEAD;
            case ACCOUNT -> ACCOUNTS_LEAD;
            case OTHER -> MAIN_ADMIN;
        };
        return userRepository.findByEmailIgnoreCase(email)
                .map(user -> user.getEmail())
                .orElse(MAIN_ADMIN);
    }

    public String describeAssignment(TicketType type) {
        return switch (type) {
            case TECHNICAL -> "IT Support Team (Team Lead triages)";
            case DATABASE -> "Database Team (Team Lead triages)";
            case HR -> "HR Team (Team Lead triages)";
            case BILLING -> "Finance Team (Team Lead triages)";
            case ACCOUNT -> "Accounts Team (Team Lead triages)";
            case OTHER -> "Support Desk Manager (manual triage)";
        };
    }

    public TicketPriority resolvePriority(TicketType type) {
        return switch (type) {
            case TECHNICAL -> TicketPriority.HIGH;
            case DATABASE -> TicketPriority.HIGH;
            case HR -> TicketPriority.MEDIUM;
            case BILLING -> TicketPriority.HIGH;
            case ACCOUNT -> TicketPriority.MEDIUM;
            case OTHER -> TicketPriority.LOW;
        };
    }

    public String describePriority(TicketType type) {
        return resolvePriority(type).name();
    }
}
