package kr.hhplus.be.server.queue.app;

import kr.hhplus.be.server.queue.domain.QueueToken;
import kr.hhplus.be.server.queue.domain.QueueTokenStatus;

import java.util.UUID;

public record QueueTokenStatusResult(QueueTokenStatus status, long currentPosition, UUID tokenValue) {

    public static QueueTokenStatusResult of(QueueToken queueToken, long currentPosition) {
        return new QueueTokenStatusResult(queueToken.getStatus(), currentPosition,queueToken.getTokenValue() );
    }
}
