package kr.hhplus.be.server.queue.domain;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class QueueToken {
    private long id;
    private UserId userId;
    private ScheduleId scheduleId;
    private UUID tokenValue;
    private QueueTokenStatus status;
    private LocalDateTime issuedAt;

    public static QueueToken createActive(
            UserId userId,
            ScheduleId scheduleId,
            LocalDateTime issuedAt) {

        return QueueToken.builder()
                .userId(userId)
                .scheduleId(scheduleId)
                .tokenValue(UUID.randomUUID())
                .status(QueueTokenStatus.ACTIVE)
                .issuedAt(issuedAt)
                .build();
    }

    public static QueueToken createWaiting(UserId userId, ScheduleId scheduleId, LocalDateTime issuedAt) {
        return QueueToken.builder()
                .userId(userId)
                .scheduleId(scheduleId)
                .tokenValue(UUID.randomUUID())
                .status(QueueTokenStatus.WAITING)
                .issuedAt(issuedAt)
                .build();
    }

    /**
     * 토큰 만료
     */
    public void expire() {
        status = QueueTokenStatus.EXPIRED;
    }

    /**
     * 토큰 재발행
     */
    public void reissue(LocalDateTime now) {
        this.issuedAt = now;
        this.status = QueueTokenStatus.ACTIVE;
        this.tokenValue = UUID.randomUUID();
    }

    /**
     * 토큰이 활성 여부 확인
     * @return boolean
     */
    public boolean isActive() {
        return status == QueueTokenStatus.ACTIVE;
    }

    /**
     * 토큰 만료 여부 확인
     * @return boolean
     */
    public boolean isExpired() {
        return status == QueueTokenStatus.EXPIRED;
    }

    /**
     * 토큰 대기상태 확인
     * @return boolean
     */
    public boolean isWaiting() {
        return status == QueueTokenStatus.WAITING;
    }
}