package com.support.tickets.controller;

import com.support.tickets.domain.TicketStatus;
import com.support.tickets.dto.CreateCommentRequest;
import com.support.tickets.dto.CreateTicketRequest;
import com.support.tickets.dto.TicketResponse;
import com.support.tickets.dto.UpdateTicketRequest;
import com.support.tickets.security.AuthenticatedUser;
import com.support.tickets.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody CreateTicketRequest request) {
        return ticketService.createTicket(request, user);
    }

    @GetMapping
    public List<TicketResponse> list(
            @AuthenticationPrincipal AuthenticatedUser user,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) String assignee) {
        return ticketService.listTickets(keyword, status, assignee, user);
    }

    @GetMapping("/{id}")
    public TicketResponse get(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id) {
        return ticketService.getTicket(id, user);
    }

    @PatchMapping("/{id}")
    public TicketResponse update(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketRequest request) {
        return ticketService.updateTicket(id, request, user);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse addComment(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long id,
            @Valid @RequestBody CreateCommentRequest request) {
        return ticketService.addComment(id, request, user);
    }
}
