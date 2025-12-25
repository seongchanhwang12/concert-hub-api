package kr.hhplus.be.server.wallet.app.dto;

import kr.hhplus.be.server.common.domain.UserId;

import java.util.UUID;

public record ChargePointCommand(UUID IdempotencyKey, UserId userId,  long amount) {

}
