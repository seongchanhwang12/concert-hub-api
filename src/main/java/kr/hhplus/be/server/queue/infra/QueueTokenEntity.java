package kr.hhplus.be.server.queue.infra;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.queue.domain.QueueToken;
import kr.hhplus.be.server.queue.domain.QueueTokenStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "queue_token", uniqueConstraints = {
        @UniqueConstraint(name="uk_token_user_key", columnNames = {"schedule_id","user_id"}),
        @UniqueConstraint(name="uk_token_value_key", columnNames = {"token_value"})
})
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueueTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "queue_token_seq_gen")
    @SequenceGenerator(
            name = "queue_token_seq_gen",
            sequenceName = "queue_token_seq",
            allocationSize = 50
    )
    private Long id;

    @Column(name = "user_id", nullable = false)
    private UUID userId ;

    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Column(name = "token_value", nullable = false)
    private UUID tokenValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_status", nullable = false)
    private QueueTokenStatus status;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    public static QueueTokenEntity from(QueueToken queueToken) {
        return QueueTokenEntity.builder()
                .scheduleId(queueToken.getId())
                .userId(queueToken.getUserId().value())
                .status(queueToken.getStatus())
                .tokenValue(queueToken.getTokenValue())
                .issuedAt(queueToken.getIssuedAt())
                .build();
    }

    public QueueToken toDomain() {
        return QueueToken.builder()
                .id(id)
                .scheduleId(ScheduleId.of(scheduleId))
                .userId(UserId.of(userId))
                .status(status)
                .tokenValue(tokenValue)
                .issuedAt(issuedAt)
                .build();
    }
}
