package kr.hhplus.be.server.wallet.app.dto;

import kr.hhplus.be.server.common.domain.Money;

import java.util.UUID;

public record ChargePointCommand(UUID IdempotencyKey, UUID userId, long amount) {
}
