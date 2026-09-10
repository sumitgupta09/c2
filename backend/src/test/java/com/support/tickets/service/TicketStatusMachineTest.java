package com.support.tickets.service;

import com.support.tickets.domain.TicketStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TicketStatusMachineTest {

    private final TicketStatusMachine machine = new TicketStatusMachine();

    @ParameterizedTest
    @MethodSource("validTransitions")
    void allowsValidTransitions(TicketStatus from, TicketStatus to) {
        assertEquals(true, machine.canTransition(from, to));
    }

    @ParameterizedTest
    @MethodSource("invalidTransitions")
    void rejectsInvalidTransitions(TicketStatus from, TicketStatus to) {
        assertEquals(false, machine.canTransition(from, to));
    }

    static Stream<Arguments> validTransitions() {
        return Stream.of(
                Arguments.of(TicketStatus.OPEN, TicketStatus.IN_PROGRESS),
                Arguments.of(TicketStatus.OPEN, TicketStatus.CANCELLED),
                Arguments.of(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED),
                Arguments.of(TicketStatus.IN_PROGRESS, TicketStatus.CANCELLED),
                Arguments.of(TicketStatus.RESOLVED, TicketStatus.CLOSED),
                Arguments.of(TicketStatus.OPEN, TicketStatus.OPEN)
        );
    }

    static Stream<Arguments> invalidTransitions() {
        return Stream.of(
                Arguments.of(TicketStatus.CLOSED, TicketStatus.OPEN),
                Arguments.of(TicketStatus.RESOLVED, TicketStatus.OPEN),
                Arguments.of(TicketStatus.CANCELLED, TicketStatus.OPEN),
                Arguments.of(TicketStatus.OPEN, TicketStatus.RESOLVED),
                Arguments.of(TicketStatus.OPEN, TicketStatus.CLOSED),
                Arguments.of(TicketStatus.RESOLVED, TicketStatus.CANCELLED)
        );
    }
}
