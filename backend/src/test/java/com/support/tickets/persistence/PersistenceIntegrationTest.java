package com.support.tickets.persistence;

import com.support.tickets.SupportTicketApplication;
import com.support.tickets.domain.Ticket;
import com.support.tickets.domain.TicketPriority;
import com.support.tickets.domain.TicketType;
import com.support.tickets.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies data survives closing and reopening the Spring context (simulates app restart with file H2).
 */
class PersistenceIntegrationTest {

    private static final String[] ARGS = {"--spring.profiles.active=persistence-test"};

    @Test
    void restartSurvives() {
        long ticketId;
        try (ConfigurableApplicationContext ctx1 = SpringApplication.run(SupportTicketApplication.class, ARGS)) {
            TicketRepository repo = ctx1.getBean(TicketRepository.class);
            Ticket ticket = new Ticket();
            ticket.setTitle("Persistence test ticket");
            ticket.setDescription("Should survive context restart");
            ticket.setTicketType(TicketType.HR);
            ticket.setPriority(TicketPriority.MEDIUM);
            ticket.setAssignee("hr.lead@acmecorp.com");
            ticket.setCreatedBy("guest");
            ticketId = repo.save(ticket).getId();
            assertThat(ticketId).isPositive();
        }

        try (ConfigurableApplicationContext ctx2 = SpringApplication.run(SupportTicketApplication.class, ARGS)) {
            TicketRepository repo = ctx2.getBean(TicketRepository.class);
            Ticket loaded = repo.findById(ticketId).orElseThrow();
            assertThat(loaded.getTitle()).isEqualTo("Persistence test ticket");
            assertThat(loaded.getTicketType()).isEqualTo(TicketType.HR);
        }
    }
}
