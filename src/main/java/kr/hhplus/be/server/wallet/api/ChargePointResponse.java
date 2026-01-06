package kr.hhplus.be.server.wallet.api;

import kr.hhplus.be.server.wallet.app.dto.ChargePointResult;

import java.util.UUID;

public record ChargePointResponse(UUID transactionId, long chargedPoint, long balanceAfter) {

    public static ChargePointResponse from(ChargePointResult result) {
        return new ChargePointResponse(result.transactionId().value(), result.chargedPoint().balance(), result.balanceAfter().balance());
    }
}
