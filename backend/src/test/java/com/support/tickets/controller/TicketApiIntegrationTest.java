package com.support.tickets.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.support.tickets.domain.TicketPriority;
import com.support.tickets.domain.TicketStatus;
import com.support.tickets.domain.TicketType;
import com.support.tickets.service.TicketAssignmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TicketApiIntegrationTest {

    private static final String MAIN_ADMIN = TicketAssignmentService.MAIN_ADMIN;
    private static final String IT_LEAD = TicketAssignmentService.IT_LEAD;
    private static final String IT_AGENT = "it.agent@acmecorp.com";
    private static final String FINANCE_AGENT = "finance.agent@acmecorp.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String login(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", password
                        ))))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("token").asText();
    }

    @Test
    void guestCanCreateTicketAndCheckStatusWithoutLogin() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Public request",
                                "description", "No login needed",
                                "ticketType", TicketType.BILLING.name()
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assignee").value(TicketAssignmentService.FINANCE_LEAD))
                .andExpect(jsonPath("$.priority").value(TicketPriority.HIGH.name()))
                .andReturn();

        long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/tickets/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OPEN"));

        mockMvc.perform(patch("/api/tickets/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "IN_PROGRESS"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userCreatesTicketAutoAssignedByType() throws Exception {
        String financeToken = login(FINANCE_AGENT, "agent123");

        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .header("Authorization", "Bearer " + financeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Login issue",
                                "description", "Cannot login with SSO",
                                "ticketType", TicketType.TECHNICAL.name()
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.assignee").value(IT_LEAD))
                .andExpect(jsonPath("$.ticketType").value("TECHNICAL"))
                .andExpect(jsonPath("$.priority").value(TicketPriority.HIGH.name()))
                .andReturn();

        long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        String itLeadToken = login(IT_LEAD, "lead123");
        mockMvc.perform(get("/api/tickets/" + id).header("Authorization", "Bearer " + itLeadToken))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + itLeadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "IN_PROGRESS"))))
                .andExpect(status().isOk());
    }

    @Test
    void otherTypeAssignsToMainAdmin() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Misc request",
                                "description", "Needs admin review",
                                "ticketType", TicketType.OTHER.name()
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assignee").value(MAIN_ADMIN))
                .andExpect(jsonPath("$.priority").value(TicketPriority.LOW.name()));
    }

    @Test
    void teamLeadCanReassignWithinTeam() throws Exception {
        String itLeadToken = login(IT_LEAD, "lead123");

        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Printer offline",
                                "description", "Floor 3 printer",
                                "ticketType", TicketType.TECHNICAL.name()
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + itLeadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("assignee", IT_AGENT))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignee").value(IT_AGENT));
    }

    @Test
    void agentCannotUpdateTicketAssignedToSomeoneElse() throws Exception {
        String financeToken = login(FINANCE_AGENT, "agent123");

        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .header("Authorization", "Bearer " + financeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "For IT team",
                                "description", "Technical",
                                "ticketType", TicketType.TECHNICAL.name()
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + financeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("title", "Hacked"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void onlyMainAdminCanChangePriority() throws Exception {
        String itLeadToken = login(IT_LEAD, "lead123");
        String mainAdminToken = login(MAIN_ADMIN, "admin123");

        MvcResult createResult = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Priority test",
                                "description", "Auto priority",
                                "ticketType", TicketType.TECHNICAL.name()
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + itLeadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("priority", TicketPriority.URGENT.name()))))
                .andExpect(status().isForbidden());

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + mainAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("priority", TicketPriority.URGENT.name()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority").value(TicketPriority.URGENT.name()));
    }

    @Test
    void listIncludesCreatedTickets() throws Exception {
        String financeToken = login(FINANCE_AGENT, "agent123");

        mockMvc.perform(post("/api/tickets")
                        .header("Authorization", "Bearer " + financeToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "List test",
                                "description", "Visible to creator",
                                "ticketType", TicketType.BILLING.name()
                        ))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tickets").header("Authorization", "Bearer " + financeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void updateTitleAndDescription() throws Exception {
        String itLeadToken = login(IT_LEAD, "lead123");
        long id = createTicketId(itLeadToken, "Original title", TicketType.TECHNICAL);

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + itLeadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "Updated title",
                                "description", "Updated description"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    void addComment() throws Exception {
        String itAgentToken = login(IT_AGENT, "agent123");
        long id = createTicketId(login(IT_LEAD, "lead123"), "Comment test", TicketType.TECHNICAL);

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + login(IT_LEAD, "lead123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("assignee", IT_AGENT))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/tickets/" + id + "/comments")
                        .header("Authorization", "Bearer " + itAgentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("body", "Working on this now"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comments", hasSize(1)))
                .andExpect(jsonPath("$.comments[0].body").value("Working on this now"));
    }

    @Test
    void searchByKeyword() throws Exception {
        String adminToken = login(MAIN_ADMIN, "admin123");
        createTicketId(adminToken, "UniqueSearchKeyword ticket", TicketType.HR);
        createTicketId(adminToken, "Other ticket", TicketType.ACCOUNT);

        mockMvc.perform(get("/api/tickets?keyword=UniqueSearchKeyword")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("UniqueSearchKeyword ticket"));
    }

    @Test
    void filterByStatus() throws Exception {
        String adminToken = login(MAIN_ADMIN, "admin123");
        long id = createTicketId(adminToken, "Filter status test", TicketType.HR);

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", TicketStatus.IN_PROGRESS.name()))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/tickets?status=IN_PROGRESS")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title=='Filter status test')]", hasSize(1)));

        mockMvc.perform(get("/api/tickets?status=OPEN")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title=='Filter status test')]", hasSize(0)));
    }

    @Test
    void validTransitionChain() throws Exception {
        String itLeadToken = login(IT_LEAD, "lead123");
        long id = createTicketId(itLeadToken, "Transition chain", TicketType.TECHNICAL);

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + itLeadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", TicketStatus.IN_PROGRESS.name()))))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + itLeadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", TicketStatus.RESOLVED.name()))))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + itLeadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", TicketStatus.CLOSED.name()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void invalidTransitionReturns409() throws Exception {
        String itLeadToken = login(IT_LEAD, "lead123");
        long id = createTicketId(itLeadToken, "Invalid transition", TicketType.TECHNICAL);

        mockMvc.perform(patch("/api/tickets/" + id)
                        .header("Authorization", "Bearer " + itLeadToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", TicketStatus.CLOSED.name()))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void blankTitleReturns400WithFieldErrors() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", "",
                                "description", "Missing title",
                                "ticketType", TicketType.TECHNICAL.name()
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.title").exists());
    }

    @Test
    void getTicketNotFoundReturns404() throws Exception {
        String adminToken = login(MAIN_ADMIN, "admin123");

        mockMvc.perform(get("/api/tickets/999999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    private long createTicketId(String token, String title, TicketType type) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/tickets")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "title", title,
                                "description", title + " description",
                                "ticketType", type.name()
                        ))))
                .andExpect(status().isCreated())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }
}
