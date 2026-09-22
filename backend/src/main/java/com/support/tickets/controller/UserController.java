package com.support.tickets.controller;

import com.support.tickets.dto.UserResponse;
import com.support.tickets.security.AuthenticatedUser;
import com.support.tickets.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/agents")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEAM_ADMIN')")
    public List<UserResponse> listAgents(@AuthenticationPrincipal AuthenticatedUser user) {
        return userService.listAssignableStaff(user);
    }
}
