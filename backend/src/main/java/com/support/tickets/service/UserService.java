package com.support.tickets.service;

import com.support.tickets.domain.UserRole;
import com.support.tickets.dto.UserResponse;
import com.support.tickets.exception.ForbiddenException;
import com.support.tickets.repository.UserRepository;
import com.support.tickets.security.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> listAssignableStaff(AuthenticatedUser user) {
        if (user.isAdmin()) {
            return userRepository.findByRoleIn(List.of(UserRole.AGENT, UserRole.TEAM_ADMIN))
                    .stream()
                    .map(UserResponse::from)
                    .toList();
        }
        if (user.isTeamAdmin()) {
            return userRepository.findByTeam(user.team())
                    .stream()
                    .map(UserResponse::from)
                    .toList();
        }
        throw new ForbiddenException("Only team leads and the support desk manager can list staff");
    }
}
