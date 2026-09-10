package com.support.tickets.config;

import com.support.tickets.domain.SupportTeam;
import com.support.tickets.domain.Ticket;
import com.support.tickets.domain.TicketPriority;
import com.support.tickets.domain.TicketStatus;
import com.support.tickets.domain.TicketType;
import com.support.tickets.domain.User;
import com.support.tickets.domain.UserRole;
import com.support.tickets.repository.TicketRepository;
import com.support.tickets.repository.UserRepository;
import com.support.tickets.service.TicketAssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Profile("!persistence-test")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    public DataSeeder(
            UserRepository userRepository,
            TicketRepository ticketRepository,
            PasswordEncoder passwordEncoder,
            Environment environment) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            seedCorporateUsers();
            log.info("Seeded Acme Corp support desk users");
        }
        boolean skipSampleTickets = Arrays.stream(environment.getActiveProfiles())
                .anyMatch(p -> p.equals("test") || p.equals("persistence-test"));
        if (!skipSampleTickets && ticketRepository.count() == 0) {
            seedSampleTickets();
            log.info("Seeded sample corporate tickets");
        }
    }

    private void seedCorporateUsers() {
        seed("support.admin@acmecorp.com", "Priya Sharma", "admin123",
                UserRole.ADMIN, SupportTeam.SUPPORT_DESK);
        seed("it.lead@acmecorp.com", "Rahul Mehta", "lead123",
                UserRole.TEAM_ADMIN, SupportTeam.IT);
        seed("it.agent@acmecorp.com", "Anita Desai", "agent123",
                UserRole.AGENT, SupportTeam.IT);
        seed("db.lead@acmecorp.com", "Vikram Singh", "lead123",
                UserRole.TEAM_ADMIN, SupportTeam.DATABASE);
        seed("db.agent@acmecorp.com", "Neha Kapoor", "agent123",
                UserRole.AGENT, SupportTeam.DATABASE);
        seed("hr.lead@acmecorp.com", "Sneha Reddy", "lead123",
                UserRole.TEAM_ADMIN, SupportTeam.HR);
        seed("hr.agent@acmecorp.com", "Arjun Nair", "agent123",
                UserRole.AGENT, SupportTeam.HR);
        seed("finance.lead@acmecorp.com", "Karan Patel", "lead123",
                UserRole.TEAM_ADMIN, SupportTeam.FINANCE);
        seed("finance.agent@acmecorp.com", "Meera Iyer", "agent123",
                UserRole.AGENT, SupportTeam.FINANCE);
        seed("accounts.lead@acmecorp.com", "Divya Rao", "lead123",
                UserRole.TEAM_ADMIN, SupportTeam.ACCOUNTS);
        seed("accounts.agent@acmecorp.com", "Rohan Gupta", "agent123",
                UserRole.AGENT, SupportTeam.ACCOUNTS);
    }

    private void seedSampleTickets() {
        sample("VPN connection failing from home office", TicketType.TECHNICAL,
                TicketPriority.HIGH, TicketStatus.OPEN, TicketAssignmentService.IT_LEAD, "guest");
        sample("Payroll report query timing out", TicketType.DATABASE,
                TicketPriority.HIGH, TicketStatus.IN_PROGRESS, "db.agent@acmecorp.com", "employee@acmecorp.com");
        sample("Annual leave balance showing incorrect days", TicketType.HR,
                TicketPriority.MEDIUM, TicketStatus.OPEN, TicketAssignmentService.HR_LEAD, "riya.k@acmecorp.com");
        sample("Vendor invoice amount mismatch for Q3", TicketType.BILLING,
                TicketPriority.HIGH, TicketStatus.OPEN, "finance.agent@acmecorp.com", "procurement@acmecorp.com");
        sample("New joiner laptop and email setup", TicketType.ACCOUNT,
                TicketPriority.MEDIUM, TicketStatus.RESOLVED, "accounts.agent@acmecorp.com", "manager@acmecorp.com");
        sample("General office access card not working", TicketType.OTHER,
                TicketPriority.LOW, TicketStatus.OPEN, TicketAssignmentService.MAIN_ADMIN, "guest");
    }

    private void sample(
            String title,
            TicketType type,
            TicketPriority priority,
            TicketStatus status,
            String assignee,
            String createdBy) {
        Ticket ticket = new Ticket();
        ticket.setTitle(title);
        ticket.setDescription(title + " — submitted via corporate support portal.");
        ticket.setTicketType(type);
        ticket.setPriority(priority);
        ticket.setStatus(status);
        ticket.setAssignee(assignee);
        ticket.setCreatedBy(createdBy);
        ticketRepository.save(ticket);
    }

    private void seed(String email, String name, String password, UserRole role, SupportTeam team) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);
        user.setTeam(team);
        userRepository.save(user);
    }
}
