package com.support.tickets.repository;

import com.support.tickets.domain.SupportTeam;
import com.support.tickets.domain.User;
import com.support.tickets.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    List<User> findByRole(UserRole role);

    List<User> findByTeam(SupportTeam team);

    List<User> findByRoleIn(Collection<UserRole> roles);
}
