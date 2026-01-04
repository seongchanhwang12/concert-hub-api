package kr.hhplus.be.server.wallet.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import kr.hhplus.be.server.common.domain.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class WalletTransaction {
    private final WalletTransactionId id;
    private final WalletId walletId;
    private final UserId ownerId;
    private Point balanceAfter;
    private Point pointAmount;
    private TransactionType type;
    private TransactionReference reference;
    private UUID idempotencyKey;

    public static WalletTransaction createCharge(
            WalletTransactionId id,
            Wallet wallet,
            Point chargeAmount,
            UUID idempotencyKey) {
        return new WalletTransaction(
                id,
                wallet.getId(),
                wallet.getOwnerId(),
                wallet.getBalance(),
                chargeAmount,
                TransactionType.CHARGE,
                null,
                idempotencyKey
                );
    }

    public static WalletTransaction createUse(
            WalletTransactionId id,
            Wallet wallet,
            Point useAmount,
            TransactionReference paymentId,
            UUID idempotencyKey) {
        return new WalletTransaction(
                id,
                wallet.getId(),
                wallet.getOwnerId(),
                wallet.getBalance(),
                useAmount,
                TransactionType.USE,
                paymentId,
                idempotencyKey);
    }

    public static WalletTransaction of(
            WalletTransactionId id,
            WalletId walletId,
            UserId userId,
            TransactionType type,
            Point pointAmount,
            Point balanceAfter,
            TransactionReference reference,
            UUID idempotencyKey) {
        return new WalletTransaction(
                id,
                walletId,
                userId,
                pointAmount,
                balanceAfter,
                type,
                reference,
                idempotencyKey);
    }
}
