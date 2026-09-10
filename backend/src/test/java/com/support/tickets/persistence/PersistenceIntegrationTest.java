package com.support.tickets.persistence;

import com.support.tickets.domain.Ticket;
import com.support.tickets.domain.TicketPriority;
import com.support.tickets.domain.TicketType;
import com.support.tickets.repository.TicketRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies ticket data survives closing and reopening the Spring context (file H2 restart).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("persistence-test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersistenceIntegrationTest {

    static long persistedTicketId;

    @Nested
    @Order(1)
    class FirstContext {

        @Autowired
        private TicketRepository ticketRepository;

        @Test
        @Order(1)
        void savesTicketToFileDatabase() {
            Ticket ticket = new Ticket();
            ticket.setTitle("Persistence test ticket");
            ticket.setDescription("Should survive context restart");
            ticket.setTicketType(TicketType.HR);
            ticket.setPriority(TicketPriority.MEDIUM);
            ticket.setAssignee("hr.lead@acmecorp.com");
            ticket.setCreatedBy("guest");
            persistedTicketId = ticketRepository.save(ticket).getId();
            assertThat(persistedTicketId).isPositive();
        }
    }

    @Nested
    @Order(2)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    class AfterRestart {

        @Autowired
        private TicketRepository ticketRepository;

        @Test
        @Order(1)
        void loadsTicketFromFileDatabase() {
            Ticket loaded = ticketRepository.findById(persistedTicketId).orElseThrow();
            assertThat(loaded.getTitle()).isEqualTo("Persistence test ticket");
            assertThat(loaded.getTicketType()).isEqualTo(TicketType.HR);
        }
    }
}
