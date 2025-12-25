package kr.hhplus.be.server.common.app;

import kr.hhplus.be.server.common.domain.UserId;

public interface AuthFacade {

    UserId currentUserId();
}
