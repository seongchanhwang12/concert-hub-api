package kr.hhplus.be.server.wallet.infra;

import jakarta.persistence.*;
import kr.hhplus.be.server.wallet.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(
    name = "wallet_transaction",
    uniqueConstraints = {
            @UniqueConstraint(name="uk_user_idempotency_key", columnNames = {"user_id","idempotency_key"})
    }
)
@AllArgsConstructor
@NoArgsConstructor
public class JpaWalletTransaction {
    @Id
    private UUID id;

    @Column(name = "wallet_id")
    private UUID walletId;

    @Column(name = "user_id")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    TransactionType type;

    @Column(name = "point_amount")
    private long pointAmount;

    @Column(name = "balance_after")
    private long balanceAfter;

    @Column(name = "idempotency_key")
    private UUID idempotencyKey;

}
