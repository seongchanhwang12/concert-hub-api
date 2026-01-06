package kr.hhplus.be.server.common.infra;

import com.github.f4b6a3.uuid.UuidCreator;
import kr.hhplus.be.server.reservation.app.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidV7IdGenerator implements IdGenerator {

    @Override
    public UUID nextId() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
