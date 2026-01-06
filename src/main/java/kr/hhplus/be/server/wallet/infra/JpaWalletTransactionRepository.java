package kr.hhplus.be.server.wallet.infra;

import kr.hhplus.be.server.wallet.domain.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface JpaWalletTransactionRepository extends JpaRepository<JpaWalletTransaction, UUID> {
    JpaWalletTransaction findByUserIdAndIdempotencyKey(UUID userId, UUID idempotencyKey);

    @Modifying
    @Query(value= """
        INSERT INTO WALLET_TRANSACTION(
            id, 
            wallet_id, 
            user_id, 
            transaction_type, 
            point_amount,
            balance_after,
            idempotency_key
            )
            VALUES (
                :id, 
                :walletId, 
                :userId, 
                :transactionType, 
                :pointAmount, 
                :balanceAfter,
                :idempotencyKey)
            ON CONFLICT (user_id, idempotency_key) DO NOTHING 
    """,nativeQuery = true)
    int saveIdempotency(
            @Param("id") UUID id,
            @Param("walletId") UUID wallet_id,
            @Param("userId") UUID userId,
            @Param("transactionType") String transactionType,
            @Param("pointAmount") long pointAmount,
            @Param("balanceAfter") long balanceAfter,
            @Param("idempotencyKey") UUID idempotencyKey
    );
}
