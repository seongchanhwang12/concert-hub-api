package kr.hhplus.be.server.queue.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FixedTimeQueueTokenExpirationPolicy implements QueueTokenExpirationPolicy {
    public LocalDateTime expiresAt(LocalDateTime issuedAt) {
        return issuedAt.plusMinutes(5);
    }
}
