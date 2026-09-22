package com.support.tickets.dto;

import com.support.tickets.domain.SupportTeam;
import com.support.tickets.domain.UserRole;

public record AuthResponse(
        String token,
        String email,
        String name,
        UserRole role,
        SupportTeam team
) {
}
