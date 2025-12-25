package kr.hhplus.be.server.wallet.infra;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.domain.WalletId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaWalletRepository extends JpaRepository<JpaWallet, UUID> {
    Optional<JpaWallet> findByOwnerId(UUID ownerId);

    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Query(value = """
        INSERT INTO wallet(id, owner_id, point)
        VALUES (:id, :ownerId, 0)
        ON CONFLICT (owner_id) DO UPDATE
        SET owner_id = EXCLUDED.owner_id
        """, nativeQuery = true)
    void upsertAndReturn(@Param("id") UUID id,
                         @Param("ownerId") UUID ownerId);


    @Query(value = """
        SELECT w.point
        FROM JpaWallet w
        WHERE w.ownerId = :userId
    """)
    Optional<Long> findPointBalanceByUserId(@Param("userId") UUID userId);
}
