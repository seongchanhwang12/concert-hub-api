package kr.hhplus.be.server.payment.app;

import kr.hhplus.be.server.common.domain.UserId;

import java.util.UUID;

public record PayReservationCommand(UUID idempotencyKey, UUID reservationId, UUID tokenValue, UserId userId) {


}