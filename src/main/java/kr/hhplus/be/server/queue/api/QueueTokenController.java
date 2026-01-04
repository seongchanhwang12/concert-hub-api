package kr.hhplus.be.server.queue.api;

import kr.hhplus.be.server.common.app.AuthFacade;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.queue.app.IssueQueueTokenCommand;
import kr.hhplus.be.server.queue.app.IssueQueueTokenResult;
import kr.hhplus.be.server.queue.app.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queues")
public class QueueTokenController {
    private final QueueService queueService;
    private final AuthFacade authFacade;

    @PostMapping("/enter")
    public ResponseEntity<IssueQueueTokenResponse> issueQueueToken(IssueQueueTokenRequest request) {
        UserId userId = authFacade.currentUserId();
        IssueQueueTokenResult result = queueService.issueQueueToken(IssueQueueTokenCommand.of(request.scheduleId(), userId));
        return ResponseEntity.ok().body(IssueQueueTokenResponse.from(result));
    }


}
