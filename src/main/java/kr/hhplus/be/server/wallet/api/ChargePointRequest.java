package kr.hhplus.be.server.wallet.api;

import java.util.UUID;

public record ChargePointRequest(UUID idempotencyKey, long amount) {
}
