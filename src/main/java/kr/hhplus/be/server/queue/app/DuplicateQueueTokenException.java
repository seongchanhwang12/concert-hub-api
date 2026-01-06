package kr.hhplus.be.server.queue.app;

import kr.hhplus.be.server.common.api.ApplicationException;

public class DuplicateQueueTokenException extends ApplicationException {
    public DuplicateQueueTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
