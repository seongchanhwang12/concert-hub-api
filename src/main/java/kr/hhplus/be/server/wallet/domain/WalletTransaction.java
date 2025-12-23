package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.common.domain.UserId;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class WalletTransaction {
    private WalletTransactionId id;
    private WalletId walletId;
    private UserId ownerId;
    private TransactionType type;
    private Point pointAmount;
    private Point balanceAfter;
    private UUID idempotencyKey;

    public static WalletTransaction createCharge(WalletTransactionId id, WalletId walletId, UserId userId, Point chargeAmount, Point balanceAfter, UUID idempotencyKey) {
        return new WalletTransaction(id, walletId, userId, TransactionType.CHARGE, chargeAmount, balanceAfter, idempotencyKey);
    }

    public static WalletTransaction of(
            WalletTransactionId id,
            WalletId walletId,
            UserId userId,
            TransactionType type,
            Point pointAmount,
            Point balanceAfter,
            UUID idempotencyKey) {
        return new WalletTransaction(id,walletId,userId,type,pointAmount,balanceAfter,idempotencyKey);
    }
}
