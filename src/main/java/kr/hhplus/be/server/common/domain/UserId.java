package kr.hhplus.be.server.common.domain;

import java.util.UUID;

public record UserId(UUID value) {

    public static UserId of(UUID value) {
        return new UserId(value);
    }
}
