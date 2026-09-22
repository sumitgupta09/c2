package com.support.tickets.repository;

import com.support.tickets.domain.Ticket;
import com.support.tickets.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
            SELECT t FROM Ticket t
            WHERE (:status IS NULL OR t.status = :status)
              AND (
                    :adminView = true
                    OR LOWER(t.assignee) = LOWER(:userEmail)
                    OR LOWER(t.createdBy) = LOWER(:userEmail)
                    OR (:teamView = true AND LOWER(t.assignee) IN :teamEmails)
                  )
              AND (:assignee IS NULL OR :assignee = ''
                   OR LOWER(t.assignee) = LOWER(:assignee))
              AND (:ticketId IS NULL OR t.id = :ticketId)
              AND (:keyword IS NULL OR :keyword = ''
                   OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
            ORDER BY t.updatedAt DESC
            """)
    List<Ticket> search(
            @Param("keyword") String keyword,
            @Param("ticketId") Long ticketId,
            @Param("status") TicketStatus status,
            @Param("assignee") String assignee,
            @Param("adminView") boolean adminView,
            @Param("teamView") boolean teamView,
            @Param("teamEmails") Set<String> teamEmails,
            @Param("userEmail") String userEmail);
}
