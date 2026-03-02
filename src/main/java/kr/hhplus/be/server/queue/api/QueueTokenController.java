package kr.hhplus.be.server.queue.api;

import kr.hhplus.be.server.common.app.AuthFacade;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.queue.app.IssueQueueTokenCommand;
import kr.hhplus.be.server.queue.app.IssueQueueTokenResult;
import kr.hhplus.be.server.queue.app.QueueService;
import kr.hhplus.be.server.queue.app.QueueTokenStatusResult;
import kr.hhplus.be.server.queue.domain.QueueToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queues")
public class QueueTokenController {
    private final QueueService queueService;
    private final AuthFacade authFacade;

    @PostMapping
    public ResponseEntity<IssueQueueTokenResponse> issueQueueToken(IssueQueueTokenRequest request) {
        UserId userId = authFacade.currentUserId();
        IssueQueueTokenResult result = queueService.issueQueueToken(IssueQueueTokenCommand.of(request.scheduleId(), userId));
        return ResponseEntity.ok().body(IssueQueueTokenResponse.from(result));
    }

    @GetMapping
    public ResponseEntity<QueueTokenStatusResponse> getQueueTokenStatus(@RequestHeader("X-Queue-Token") UUID tokenValue) {
        QueueTokenStatusResult result = queueService.checkQueueTokenStatus(tokenValue);
        return ResponseEntity.ok().body(QueueTokenStatusResponse.from(result));
    }



}
