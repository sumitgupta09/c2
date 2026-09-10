package com.support.tickets.service;

import com.support.tickets.domain.User;
import com.support.tickets.dto.AuthResponse;
import com.support.tickets.dto.LoginRequest;
import com.support.tickets.exception.ForbiddenException;
import com.support.tickets.repository.UserRepository;
import com.support.tickets.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new ForbiddenException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ForbiddenException("Invalid email or password");
        }

        return new AuthResponse(
                jwtService.generateToken(user),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getTeam()
        );
    }
}
