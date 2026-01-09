package kr.hhplus.be.server.queue.infra;

import kr.hhplus.be.server.queue.domain.QueueToken;
import kr.hhplus.be.server.queue.domain.QueueTokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface QueueTokenJpaRepository extends JpaRepository<QueueTokenEntity, UUID> {

    @Query("""
       SELECT q
       FROM QueueTokenEntity q
       WHERE q.userId = :userId
        AND q.scheduleId = :scheduleId
    """)
    Optional<QueueTokenEntity> findByUserIdAndScheduleId(@Param("userId") UUID userId, @Param("scheduleId") long scheduleId);


    @Query("""
        SELECT count(q) + 1
        FROM QueueTokenEntity q
        WHERE q.scheduleId = :scheduleId
        AND q.status = 'WAITING'
        AND (q.issuedAt < :issuedAt
            OR (q.issuedAt = :issuedAt AND q.id < :tokenId))
    """)
    long findCurrentPositionByScheduleId(
            @Param("tokenId") Long tokenId,
            @Param("scheduleId") Long scheduleId,
            @Param("status") QueueTokenStatus status,
            @Param("myIssuedAt") LocalDateTime issuedAt);

    @Query("""
        SELECT count(q)
        FROM QueueTokenEntity q
        WHERE q.scheduleId = :scheduleId
        AND q.status = 'ACTIVE'
    """)
    long countActiveTokens(long scheduleId);

    Optional<QueueTokenEntity> findByTokenValue(UUID tokenValue);
}
