package kr.hhplus.be.server.common.infra.security;

import kr.hhplus.be.server.common.app.AuthFacade;
import kr.hhplus.be.server.common.domain.UserId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtAuthFacade implements AuthFacade {

    @Override
    public UserId currentUserId() {
        // TODO : JWT 구현 예정 (임시로 1번 유저 반환)
        return UserId.of(UUID.fromString("00000000-0000-0000-0000-000000000001"));
    }
}
