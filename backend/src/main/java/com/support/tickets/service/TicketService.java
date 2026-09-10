package com.support.tickets.service;

import com.support.tickets.domain.Comment;
import com.support.tickets.domain.Ticket;
import com.support.tickets.domain.TicketStatus;
import com.support.tickets.dto.CreateCommentRequest;
import com.support.tickets.dto.CreateTicketRequest;
import com.support.tickets.dto.TicketResponse;
import com.support.tickets.dto.UpdateTicketRequest;
import com.support.tickets.exception.ForbiddenException;
import com.support.tickets.exception.InvalidStatusTransitionException;
import com.support.tickets.exception.ResourceNotFoundException;
import com.support.tickets.repository.TicketRepository;
import com.support.tickets.security.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketStatusMachine statusMachine;
    private final TicketAuthorizationService authorizationService;
    private final TicketAssignmentService assignmentService;

    public TicketService(
            TicketRepository ticketRepository,
            TicketStatusMachine statusMachine,
            TicketAuthorizationService authorizationService,
            TicketAssignmentService assignmentService) {
        this.ticketRepository = ticketRepository;
        this.statusMachine = statusMachine;
        this.authorizationService = authorizationService;
        this.assignmentService = assignmentService;
    }

    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request, AuthenticatedUser user) {
        Ticket ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setTicketType(request.ticketType());
        ticket.setPriority(assignmentService.resolvePriority(request.ticketType()));
        ticket.setAssignee(assignmentService.resolveAssignee(request.ticketType()));
        ticket.setCreatedBy(resolveCreatedBy(request, user));
        return TicketResponse.from(ticketRepository.save(ticket));
    }

    private String resolveCreatedBy(CreateTicketRequest request, AuthenticatedUser user) {
        if (user != null) {
            return user.email();
        }
        if (request.reporterEmail() != null && !request.reporterEmail().isBlank()) {
            return request.reporterEmail().trim();
        }
        return "guest";
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> listTickets(
            String keyword, TicketStatus status, String assignee, AuthenticatedUser user) {
        if (user == null) {
            throw new ForbiddenException("Authentication required");
        }
        String normalizedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        Long ticketId = null;
        if (normalizedKeyword != null && normalizedKeyword.matches("\\d+")) {
            ticketId = Long.parseLong(normalizedKeyword);
            normalizedKeyword = null;
        }
        String assigneeFilter = authorizationService.isAdmin(user) && assignee != null && !assignee.isBlank()
                ? assignee.trim() : null;
        boolean teamView = authorizationService.isTeamAdmin(user);
        var teamEmails = teamView
                ? java.util.Set.copyOf(authorizationService.teamMemberEmails(user.team()))
                : java.util.Set.of("__no_team_members__");
        return ticketRepository.search(
                        normalizedKeyword,
                        ticketId,
                        status,
                        assigneeFilter,
                        authorizationService.isAdmin(user),
                        teamView,
                        teamEmails,
                        user.email())
                .stream()
                .map(TicketResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicket(Long id, AuthenticatedUser user) {
        Ticket ticket = findTicketOrThrow(id);
        authorizationService.requireCanViewTicket(ticket, user);
        return TicketResponse.from(ticket);
    }

    @Transactional
    public TicketResponse updateTicket(Long id, UpdateTicketRequest request, AuthenticatedUser user) {
        Ticket ticket = findTicketOrThrow(id);
        authorizationService.requireCanModifyTicket(ticket, user);

        if (request.title() != null) {
            ticket.setTitle(request.title());
        }
        if (request.description() != null) {
            ticket.setDescription(request.description());
        }
        if (request.priority() != null && request.priority() != ticket.getPriority()) {
            if (!user.isAdmin()) {
                throw new ForbiddenException("Only admins can change ticket priority");
            }
            ticket.setPriority(request.priority());
        }
        if (request.assignee() != null) {
            String newAssignee = authorizationService.normalize(request.assignee());
            if (newAssignee == null || newAssignee.isBlank()) {
                throw new ForbiddenException("Assignee is required");
            }
            authorizationService.requireCanReassign(ticket, user, newAssignee);
            ticket.setAssignee(newAssignee);
        }
        if (request.status() != null && request.status() != ticket.getStatus()) {
            if (!statusMachine.canTransition(ticket.getStatus(), request.status())) {
                throw new InvalidStatusTransitionException(ticket.getStatus(), request.status());
            }
            ticket.setStatus(request.status());
        }

        return TicketResponse.from(ticketRepository.save(ticket));
    }

    @Transactional
    public TicketResponse addComment(Long ticketId, CreateCommentRequest request, AuthenticatedUser user) {
        Ticket ticket = findTicketOrThrow(ticketId);
        authorizationService.requireCanModifyTicket(ticket, user);

        Comment comment = new Comment();
        comment.setAuthor(user.email());
        comment.setBody(request.body());
        ticket.addComment(comment);
        return TicketResponse.from(ticketRepository.save(ticket));
    }

    private Ticket findTicketOrThrow(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + id));
    }
}
