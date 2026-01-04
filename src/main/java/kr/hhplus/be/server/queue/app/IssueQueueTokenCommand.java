package kr.hhplus.be.server.queue.app;

import kr.hhplus.be.server.common.domain.UserId;

public record IssueQueueTokenCommand(long scheduleId, UserId userId) {

}
