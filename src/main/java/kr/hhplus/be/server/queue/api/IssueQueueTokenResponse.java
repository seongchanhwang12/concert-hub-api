package kr.hhplus.be.server.queue.api;

import kr.hhplus.be.server.queue.app.IssueQueueTokenResult;

import java.util.UUID;

public record IssueQueueTokenResponse(UUID tokenValue, long scheduleId, UUID userId, String queueTokenStatus){

    public static IssueQueueTokenResponse from(IssueQueueTokenResult result) {
        return new IssueQueueTokenResponse(
                result.tokenValue(),
                result.scheduleId().value(),
                result.userId().value(),
                result.status().name());
    }
}
