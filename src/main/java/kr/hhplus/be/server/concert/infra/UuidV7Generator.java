package kr.hhplus.be.server.concert.infra;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import kr.hhplus.be.server.concert.domain.IdGenerator;

import java.util.UUID;

public class UuidV7Generator implements IdGenerator {

    private final TimeBasedEpochGenerator generator;

    public UuidV7Generator() {
        this.generator = Generators.timeBasedEpochGenerator();
    }

    @Override
    public UUID nextId() {
        return generator.generate();
    }
}
