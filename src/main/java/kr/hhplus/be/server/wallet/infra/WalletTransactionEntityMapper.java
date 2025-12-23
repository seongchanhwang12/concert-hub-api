package kr.hhplus.be.server.wallet.infra;

import jakarta.websocket.PongMessage;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.domain.*;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WalletTransactionEntityMapper {

    public WalletTransaction toDomain(JpaWalletTransaction entity){
        return WalletTransaction.of(
                WalletTransactionId.of(entity.getId()),
                WalletId.of(entity.getWalletId()),
                UserId.of(entity.getUserId()),
                entity.getType(),
                Point.of(entity.getPointAmount()),
                Point.of(entity.getBalanceAfter()),
                entity.getIdempotencyKey());
    }

    
    public JpaWalletTransaction toEntity(WalletTransaction walletTransaction){
        UUID id = fromTransactionId(walletTransaction.getId());
        UUID walletId = fromWalletId(walletTransaction.getWalletId());
        UUID userId = fromUserId(walletTransaction.getOwnerId());
        long amount = fromPointAmount(walletTransaction.getPointAmount());
        long balanceAfter = fromBalanceAfter(walletTransaction.getBalanceAfter());
        return new JpaWalletTransaction(id,walletId,userId,walletTransaction.getType(),amount,balanceAfter,walletTransaction.getIdempotencyKey());
    }

    public UUID fromTransactionId(WalletTransactionId id) {
        return id.value();
    }

    public UUID fromWalletId(WalletId id) {
        return id.value();
    }

    public UUID fromUserId(UserId id) {
        return id.value();
    }

    public long fromPointAmount(Point pointAmount) {
        return pointAmount.balance();
    }

    public long fromBalanceAfter(Point balanceAfter) {
        return balanceAfter.balance();
    }

}
