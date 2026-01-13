package kr.hhplus.be.server.queue.app;

import kr.hhplus.be.server.common.app.CommonErrorCode;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.common.domain.exception.ApplicationException;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.payment.domain.QueueTokenRepository;
import kr.hhplus.be.server.queue.domain.QueueToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueTokenRepository queueTokenRepository;
    private final Clock clock;
    private final static int MAX_ACTIVE_TOKEN_COUNT = 50;

    /**
     * 대기열 토큰 발급
     * - 대기 순번이 있는 경우 WAITING 발급
     * - 대기 순번 없는 경우 ACTIVE 발급
     */
    public IssueQueueTokenResult issueQueueToken(IssueQueueTokenCommand cmd) {
        final ScheduleId scheduleId = ScheduleId.of(cmd.scheduleId());
        final UserId userId = cmd.userId();
        LocalDateTime now = LocalDateTime.now(clock);

        // 이미 존재하는 토큰인 경우
        Optional<QueueToken> token = queueTokenRepository.findByUserIdAndScheduleId(userId, scheduleId);
        if(token.isPresent()) {
            QueueToken found = token.get();

            // 만료된 토큰이면 재발행
            if(found.isExpired()){
                QueueToken queueToken = reissueToken(found,LocalDateTime.now(clock));
                return IssueQueueTokenResult.from(queueToken);
            }

            // 그 외 (활성, 대기 상태 반환)
            return IssueQueueTokenResult.from(found);

        }

        // 최초 발급시 동시성 발급 처리 (scheduleId + userId 유니크 위반시 DuplicateQueueTokenException)
        try{
            long currentActiveTokenCount = queueTokenRepository.countActiveTokens(scheduleId);
            QueueToken created = currentActiveTokenCount < MAX_ACTIVE_TOKEN_COUNT
                    ? QueueToken.createActive(userId, scheduleId, now)
                    : QueueToken.createWaiting(userId, scheduleId, now);

            QueueToken saved = queueTokenRepository.saveAndFlush(created);
            return IssueQueueTokenResult.from(saved);


        } catch (DuplicateQueueTokenException e) {
            // 동시성 예외시 저장된 토큰 Unique 예외처리
            log.warn("Duplicate Queue token. scheduleId={}, userId={}",scheduleId, userId);
            QueueToken found = queueTokenRepository.findByUserIdAndScheduleId(userId, scheduleId)
                    .orElseThrow(()-> new IllegalStateException("occurred duplicate token exception. but No active token found"));

            if(found.isExpired()){
                QueueToken queueToken = reissueToken(found,LocalDateTime.now(clock));
                return IssueQueueTokenResult.from(queueToken);
            }

            return IssueQueueTokenResult.from(found);
        }

    }

    private QueueToken reissueToken(QueueToken queueToken, LocalDateTime now) {
        queueToken.reissue(now);
        return queueTokenRepository.save(queueToken);
    }

    public QueueTokenStatusResult checkQueueTokenStatus(UUID tokenValue){
        LocalDateTime now = LocalDateTime.now(clock);
        if(tokenValue == null) throw new ApplicationException(CommonErrorCode.NOT_FOUND,"queue token id is null");
        QueueToken queueToken = queueTokenRepository.findByTokenValue(tokenValue)
                .orElseThrow(() -> new ApplicationException(CommonErrorCode.NOT_FOUND, "queue token not found"));

        long tokenPosition = queueTokenRepository.findCurrentPosition(queueToken);

        if(queueToken.isActive()){
            return QueueTokenStatusResult.of(queueToken,tokenPosition);
        }

        long activeTokenCount = queueTokenRepository.countActiveTokens(queueToken.getScheduleId());

        // 현재 토큰이 waiting상태이고
        if(queueToken.isWaiting() && tokenPosition == 1 && activeTokenCount < MAX_ACTIVE_TOKEN_COUNT){
            queueToken.activate(now);
            queueTokenRepository.save(queueToken);
            return QueueTokenStatusResult.of(queueToken,tokenPosition);
        }

        return QueueTokenStatusResult.of(queueToken,tokenPosition);

    }

}
