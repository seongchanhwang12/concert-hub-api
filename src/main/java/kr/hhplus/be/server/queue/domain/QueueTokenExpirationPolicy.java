package kr.hhplus.be.server.queue.domain;

import java.time.LocalDateTime;

public interface QueueTokenExpirationPolicy {
    LocalDateTime expiresAt(LocalDateTime issuedAt);
}
