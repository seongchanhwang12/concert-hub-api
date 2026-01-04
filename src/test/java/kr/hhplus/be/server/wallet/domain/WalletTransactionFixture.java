package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.common.domain.UserId;

import java.util.UUID;

public class WalletTransactionFixture {

    public static WalletTransaction createCharge(
            Wallet wallet,
            Point chargedAmount,
            UUID idempotencyKey) {

        WalletTransactionId id = WalletTransactionId.of(UUID.randomUUID());

        return WalletTransaction.createCharge(id,
                wallet,
                chargedAmount,
                idempotencyKey);
    }

    public static WalletTransaction createUse(Wallet wallet, long useAmount, UUID idempotencyKey, String reference) {
        WalletTransactionId id = WalletTransactionId.of(UUID.randomUUID());
        Point point = Point.of(useAmount);
        return WalletTransaction.createUse(id,
                wallet,
                point,
                new TransactionReference(reference),
                idempotencyKey);
    }
}