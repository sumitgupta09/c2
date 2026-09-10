package com.support.tickets.service;

import com.support.tickets.domain.SupportTeam;
import com.support.tickets.domain.Ticket;
import com.support.tickets.domain.User;
import com.support.tickets.exception.ForbiddenException;
import com.support.tickets.repository.UserRepository;
import com.support.tickets.security.AuthenticatedUser;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketAuthorizationService {

    private final UserRepository userRepository;

    public TicketAuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void requireCanViewTicket(Ticket ticket, AuthenticatedUser user) {
        if (user == null) {
            return;
        }
        if (user.isAdmin()) {
            return;
        }
        if (isAssignee(ticket, user) || isCreator(ticket, user)) {
            return;
        }
        if (user.isTeamAdmin() && isAssigneeInTeam(ticket, user.team())) {
            return;
        }
        throw new ForbiddenException("You do not have access to this ticket");
    }

    public void requireCanModifyTicket(Ticket ticket, AuthenticatedUser user) {
        if (user == null) {
            throw new ForbiddenException("Authentication required");
        }
        if (user.isAdmin()) {
            return;
        }
        if (isAssignee(ticket, user)) {
            return;
        }
        if (user.isTeamAdmin() && isAssigneeInTeam(ticket, user.team())) {
            return;
        }
        throw new ForbiddenException(
                "Only the assignee, team lead, or support desk manager can modify this ticket");
    }

    public void requireCanReassign(Ticket ticket, AuthenticatedUser user, String newAssigneeEmail) {
        if (user.isAdmin()) {
            return;
        }
        if (!user.isTeamAdmin()) {
            throw new ForbiddenException("Only team leads and the support desk manager can reassign tickets");
        }
        if (!isAssigneeInTeam(ticket, user.team())) {
            throw new ForbiddenException("You can only reassign tickets within your team queue");
        }
        User target = userRepository.findByEmailIgnoreCase(newAssigneeEmail)
                .orElseThrow(() -> new ForbiddenException("Assignee not found"));
        if (target.getTeam() != user.team()) {
            throw new ForbiddenException("Team leads can only assign tickets within their own team");
        }
    }

    public boolean isAdmin(AuthenticatedUser user) {
        return user != null && user.isAdmin();
    }

    public boolean isTeamAdmin(AuthenticatedUser user) {
        return user != null && user.isTeamAdmin();
    }

    public List<String> teamMemberEmails(SupportTeam team) {
        return userRepository.findByTeam(team).stream()
                .map(User::getEmail)
                .map(String::toLowerCase)
                .toList();
    }

    public String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isAssignee(Ticket ticket, AuthenticatedUser user) {
        return ticket.getAssignee() != null
                && ticket.getAssignee().equalsIgnoreCase(user.email());
    }

    private boolean isCreator(Ticket ticket, AuthenticatedUser user) {
        return ticket.getCreatedBy() != null
                && ticket.getCreatedBy().equalsIgnoreCase(user.email());
    }

    private boolean isAssigneeInTeam(Ticket ticket, SupportTeam team) {
        return userRepository.findByEmailIgnoreCase(ticket.getAssignee())
                .map(user -> user.getTeam() == team)
                .orElse(false);
    }
}
