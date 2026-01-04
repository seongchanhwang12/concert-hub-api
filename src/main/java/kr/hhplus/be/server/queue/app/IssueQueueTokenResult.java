package kr.hhplus.be.server.queue.app;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.queue.domain.QueueToken;
import kr.hhplus.be.server.queue.domain.QueueTokenStatus;

import java.util.UUID;

public record IssueQueueTokenResult(UUID tokenValue,
                                    ScheduleId scheduleId,
                                    UserId userId,
                                    QueueTokenStatus status) {

    public static IssueQueueTokenResult from(QueueToken queueToken) {
        return new IssueQueueTokenResult(
                queueToken.getTokenValue(),
                queueToken.getScheduleId(),
                queueToken.getUserId(),
                queueToken.getStatus());
    }

}
