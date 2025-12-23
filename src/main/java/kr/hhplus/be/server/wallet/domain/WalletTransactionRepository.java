package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.common.domain.UserId;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletTransactionRepository {
    void save(WalletTransaction walletTransaction);

    boolean trySaveIdempotency(WalletTransaction walletTransaction);

    Optional<WalletTransaction> findByOwnerIdAndIdempotencyKey(UserId userId, UUID idempotencyKey);
}
