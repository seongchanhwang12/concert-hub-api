package kr.hhplus.be.server.concert.app.domain;

import kr.hhplus.be.server.common.domain.UUIDGenerator;

import java.util.UUID;

public class FakeIdGenerator implements UUIDGenerator {
    @Override
    public UUID nextId() {
        return UUID.fromString("00000000-0000-7000-8000-000000000001");
    }
}
