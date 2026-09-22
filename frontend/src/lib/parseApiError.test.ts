import { describe, it } from "node:test";
import assert from "node:assert/strict";
import { parseApiError } from "./parseApiError.ts";

describe("parseApiError", () => {
  it("uses backend message and fieldErrors for validation failures", () => {
    const err = parseApiError(
      { message: "Validation failed", fieldErrors: { title: "must not be blank" } },
      400
    );
    assert.equal(err.message, "Validation failed");
    assert.equal(err.fieldErrors.title, "must not be blank");
  });

  it("surfaces state-machine 409 message (not a generic failure)", () => {
    const err = parseApiError(
      { message: "Invalid status transition from CLOSED to OPEN", fieldErrors: {} },
      409
    );
    assert.match(err.message, /CLOSED to OPEN/);
  });

  it("falls back for empty body on 401", () => {
    const err = parseApiError({}, 401);
    assert.match(err.message, /Session expired/i);
  });
});
