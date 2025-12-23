package kr.hhplus.be.server.wallet.infra;

import jakarta.persistence.*;
import kr.hhplus.be.server.wallet.domain.Wallet;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Table(
    name="wallet",
    uniqueConstraints = {
            @UniqueConstraint(name="uk_wallet_owner", columnNames = {"owner_id"})
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaWallet {

    @Id
    private UUID id;

    @Column(name = "owner_id")
    private UUID ownerId;

    private long point;

    @Builder(access = AccessLevel.PROTECTED)
    public JpaWallet(UUID id, UUID ownerId, long point) {
        this.id = id;
        this.ownerId = ownerId;
        this.point = point;
    }

    public static JpaWallet from(Wallet wallet) {
        return new JpaWallet(wallet.getId().value(),
                wallet.getOwnerId().value(),
                wallet.getPoint().balance());
    }

}
