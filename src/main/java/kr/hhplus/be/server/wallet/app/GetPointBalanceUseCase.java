package kr.hhplus.be.server.wallet.app;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.domain.Point;

public interface GetPointBalanceUseCase {
    Point get(UserId userId);
}
