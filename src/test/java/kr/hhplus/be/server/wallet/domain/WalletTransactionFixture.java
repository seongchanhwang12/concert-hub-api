package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.common.domain.UserId;

import java.util.UUID;

public class WalletTransactionFixture {

    public static WalletTransaction createCharge(
            UserId userId,
            Point chargedAmount,
            Point balanceAfter,
            UUID idempotencyKey) {

        WalletTransactionId id = WalletTransactionId.of(UUID.randomUUID());
        WalletId walletId = WalletId.of(UUID.randomUUID());

        return WalletTransaction.createCharge(id,
                walletId,
                userId,
                chargedAmount,
                balanceAfter,
                idempotencyKey);
    }

}