package kr.hhplus.be.server.queue.infra;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.payment.domain.QueueTokenRepository;
import kr.hhplus.be.server.queue.app.DuplicateQueueTokenException;
import kr.hhplus.be.server.queue.domain.QueueToken;
import kr.hhplus.be.server.queue.domain.QueueTokenStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QueueTokenRepositoryAdapter implements QueueTokenRepository {

    private final QueueTokenJpaRepository queueTokenJpaRepository;

    @Override
    public long findCurrentPosition(UUID tokenId, ScheduleId scheduleId, LocalDateTime issuedAt) {
        return queueTokenJpaRepository.findCurrentPositionByScheduleId(
                tokenId,
                scheduleId.value(),
                QueueTokenStatus.ACTIVE,
                issuedAt);
    }

    @Override
    public Optional<QueueToken> findById(UUID tokenId) {
        return queueTokenJpaRepository.findById(tokenId)
                .map(QueueTokenEntity::toDomain);
    }

    @Override
    public QueueToken save(QueueToken queueToken) {
        return queueTokenJpaRepository.save(QueueTokenEntity.from(queueToken))
                .toDomain();
    }

    @Override
    public QueueToken saveAndFlush(QueueToken queueToken) {
        try {
            QueueToken saved = queueTokenJpaRepository.save(QueueTokenEntity.from(queueToken))
                    .toDomain();
            queueTokenJpaRepository.flush();
            return saved;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateQueueTokenException("save fail. Token already exists. scheduleId [" + queueToken.getScheduleId() +"], userId = ["+queueToken.getUserId()+"]" , e);
        }
    }

    @Override
    public Optional<QueueToken> findByUserIdAndScheduleId(UserId userId, ScheduleId scheduleId) {
        return queueTokenJpaRepository.findByUserIdAndScheduleId(userId.value(),scheduleId.value())
                .map(QueueTokenEntity::toDomain);
    }

}
