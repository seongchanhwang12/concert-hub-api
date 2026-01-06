package kr.hhplus.be.server.reservation.api;

import jakarta.validation.constraints.NotEmpty;

public record ReservationRequest(@NotEmpty Long seatId) { }
