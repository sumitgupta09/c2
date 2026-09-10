# Prompt — Thursday, Sep 10, 2026, 6:11 PM (UTC+5:30)

You are a Senior Software Engineer, Solution Architect, QA Engineer, and AI-assisted development mentor.

 We are going to build a Support Ticket Management System, but this task is primarily an assessment of HOW the system is engineered using AI and Spec-Driven Development.

 IMPORTANT DEVELOPMENT WORKFLOW:

 Requirement\
 ↓\
 Specification\
 ↓\
 Plan / Tasks\
 ↓\
 Implementation\
 ↓\
 Testing\
 ↓\
 Review\
 ↓\
 Fix

 DO NOT start by building the complete application.

 For this task, ONLY perform the Requirement Analysis, Specification, Project AI-Steering setup, and Implementation Planning phases.

 DO NOT implement the application yet.\
 DO NOT generate the complete Java backend.\
 DO NOT generate the complete React/Next.js frontend.\
 DO NOT create all controllers/services/repositories yet.

 ==================================================

 1. ASSIGNMENT CONTEXT\
    ==================================================

 Technology stack:

 - Java 21
- Spring Boot
- PostgreSQL for persistent runtime database
- H2 where appropriate for automated tests
- REST API
- React / Next.js or equivalent frontend
- Cursor as the primary AI development environment
- GitHub Copilot may also be used

 The application is a Support Ticket Management System.

 Functional requirements:

 1. Create a ticket.
2. List tickets.
3. View ticket details.
4. Update ticket title.
5. Update ticket description.
6. Update ticket priority.
7. Update ticket assignee.
8. Add comments.
9. Search tickets by keyword.
10. Filter tickets by status.
11. Persist ticket data in a database.
12. Validate input at the backend.
13. Display meaningful errors in the UI.

 # ==================================================\
 2\. STATE MACHINE

 The backend MUST enforce this state machine:

 OPEN → IN\_PROGRESS → RESOLVED → CLOSED

 Additional valid transitions:

 OPEN → CANCELLED\
 IN\_PROGRESS → CANCELLED

 Only the explicitly defined transitions are valid.

 Examples of invalid transitions:

 CLOSED → O
