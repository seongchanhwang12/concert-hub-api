package kr.hhplus.be.server.queue.app;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.payment.domain.QueueTokenRepository;
import kr.hhplus.be.server.queue.domain.QueueToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueTokenRepository queueTokenRepository;
    private final Clock clock;

    /**
     * 대기열 토큰 발급
     * - 토큰 현재 순서 저장
     * -
     */
    public IssueQueueTokenResult issueQueueToken(IssueQueueTokenCommand cmd) {
        final ScheduleId scheduleId = ScheduleId.of(cmd.scheduleId());
        final UserId userId = cmd.userId();
        LocalDateTime now = LocalDateTime.now(clock);

        // 이미 존재하는 토큰인 경우
        Optional<QueueToken> token = queueTokenRepository.findByUserIdAndScheduleId(userId, scheduleId);
        if(token.isPresent()) {
            QueueToken found = token.get();
            QueueToken queueToken = ensureActive(found,LocalDateTime.now(clock));
            return IssueQueueTokenResult.from(queueToken);
        }

        // 최초 발급시 동시성 발급 처리
        try{
            QueueToken saved = queueTokenRepository.saveAndFlush(
                    QueueToken.createActive(userId,scheduleId,now));
            return IssueQueueTokenResult.from(saved);
        } catch (DuplicateQueueTokenException e) {
            // 동시성 예외시 저장된 토큰 Unique 예외처리
            log.warn("Duplicate Queue token. scheduleId={}, userId={}",scheduleId, userId);
            QueueToken found = queueTokenRepository.findByUserIdAndScheduleId(userId, scheduleId)
                    .orElseThrow(()-> new IllegalStateException("occurred duplicate token exception. but No active token found"));

            QueueToken queueToken = ensureActive(found, LocalDateTime.now(clock));
            return IssueQueueTokenResult.from(queueToken);
        }

    }

    private QueueToken ensureActive(QueueToken queueToken, LocalDateTime now) {
        if(queueToken.isActive()){
            return queueToken;
        }

        queueToken.reissue(now);
        return queueTokenRepository.save(queueToken);

    }
}
