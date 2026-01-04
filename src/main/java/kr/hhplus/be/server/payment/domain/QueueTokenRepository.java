package kr.hhplus.be.server.payment.domain;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.queue.domain.QueueToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface QueueTokenRepository {

    long findCurrentPosition(UUID tokenId, ScheduleId scheduleId, LocalDateTime now);

    Optional<QueueToken> findById(UUID tokenId);

    QueueToken saveAndFlush(QueueToken queueToken);

    Optional<QueueToken> findByUserIdAndScheduleId(UserId userId, ScheduleId scheduleId);

    QueueToken save(QueueToken found);

    long countActiveTokens(ScheduleId scheduleId);

}
