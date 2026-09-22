import { describe, it } from "node:test";
import assert from "node:assert/strict";
import { canTransitionUi, STATUS_TRANSITIONS } from "./statusTransitions.ts";
import type { TicketStatus } from "./types.ts";

describe("STATUS_TRANSITIONS (UI mirror of backend machine)", () => {
  it("allows the required happy path and cancel edges", () => {
    assert.equal(canTransitionUi("OPEN", "IN_PROGRESS"), true);
    assert.equal(canTransitionUi("IN_PROGRESS", "RESOLVED"), true);
    assert.equal(canTransitionUi("RESOLVED", "CLOSED"), true);
    assert.equal(canTransitionUi("OPEN", "CANCELLED"), true);
    assert.equal(canTransitionUi("IN_PROGRESS", "CANCELLED"), true);
    assert.equal(canTransitionUi("OPEN", "OPEN"), true);
  });

  it("rejects assignment-critical invalid transitions", () => {
    assert.equal(canTransitionUi("CLOSED", "OPEN"), false);
    assert.equal(canTransitionUi("RESOLVED", "OPEN"), false);
    assert.equal(canTransitionUi("CANCELLED", "OPEN"), false);
    assert.equal(canTransitionUi("OPEN", "RESOLVED"), false);
    assert.equal(canTransitionUi("OPEN", "CLOSED"), false);
  });

  it("terminal states have no outbound options", () => {
    assert.deepEqual(STATUS_TRANSITIONS.CLOSED, []);
    assert.deepEqual(STATUS_TRANSITIONS.CANCELLED, []);
  });

  it("covers every TicketStatus key", () => {
    const keys = Object.keys(STATUS_TRANSITIONS) as TicketStatus[];
    assert.deepEqual(keys.sort(), ["CANCELLED", "CLOSED", "IN_PROGRESS", "OPEN", "RESOLVED"]);
  });
});
