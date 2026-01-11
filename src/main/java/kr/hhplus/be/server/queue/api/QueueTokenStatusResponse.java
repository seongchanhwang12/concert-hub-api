package kr.hhplus.be.server.queue.api;

import kr.hhplus.be.server.queue.app.QueueTokenStatusResult;
import kr.hhplus.be.server.queue.domain.QueueTokenStatus;

import java.util.UUID;

public record QueueTokenStatusResponse(UUID tokenValue, QueueTokenStatus status, long currentPosition) {

    public static QueueTokenStatusResponse from(QueueTokenStatusResult result) {
        return new QueueTokenStatusResponse(result.tokenValue(),result.status(),result.currentPosition());
    }
}
