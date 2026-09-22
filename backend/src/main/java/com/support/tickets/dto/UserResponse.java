package com.support.tickets.dto;

import com.support.tickets.domain.SupportTeam;
import com.support.tickets.domain.User;
import com.support.tickets.domain.UserRole;

public record UserResponse(
        Long id,
        String email,
        String name,
        UserRole role,
        SupportTeam team
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getTeam());
    }
}
