package com.support.tickets.security;

import com.support.tickets.domain.SupportTeam;
import com.support.tickets.domain.UserRole;

public record AuthenticatedUser(
        Long id,
        String email,
        String name,
        UserRole role,
        SupportTeam team
) {
    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isTeamAdmin() {
        return role == UserRole.TEAM_ADMIN;
    }

    public boolean isAgent() {
        return role == UserRole.AGENT;
    }
}
