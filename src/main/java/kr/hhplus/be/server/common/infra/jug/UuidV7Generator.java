package kr.hhplus.be.server.common.infra.jug;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import kr.hhplus.be.server.common.domain.UUIDGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidV7Generator implements UUIDGenerator {

    private final TimeBasedEpochGenerator generator = Generators.timeBasedEpochGenerator();

    @Override
    public UUID nextId() {
        return generator.generate();
    }
}
