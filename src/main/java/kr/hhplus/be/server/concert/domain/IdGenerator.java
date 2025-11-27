package kr.hhplus.be.server.concert.domain;

import java.util.UUID;

public interface IdGenerator {
    public UUID nextId();
}
