package kr.hhplus.be.server.payment.domain;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.queue.domain.QueueToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

public interface QueueTokenRepository {

    long findCurrentPosition(QueueToken queueToken);

    Optional<QueueToken> findById(UUID tokenId);

    QueueToken saveAndFlush(QueueToken queueToken);

    Optional<QueueToken> findByUserIdAndScheduleId(UserId userId, ScheduleId scheduleId);

    QueueToken save(QueueToken found);

    long countActiveTokens(ScheduleId scheduleId);

    Optional<QueueToken>  findByTokenValue(UUID tokenValue);
}
