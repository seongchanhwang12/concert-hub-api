package kr.hhplus.be.server.wallet.app.dto;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.domain.Point;

import java.util.UUID;

public class ChargePointCommandFixture {

    public static ChargePointCommand createWith(UUID idempotencyKey, UserId userId, Point chargeAmount) {
        return new ChargePointCommand(idempotencyKey,userId, chargeAmount.balance());

    }

}