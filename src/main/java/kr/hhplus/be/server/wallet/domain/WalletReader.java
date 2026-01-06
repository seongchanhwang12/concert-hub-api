package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.common.domain.UserId;

public interface WalletReader {

    Point findPointBalanceByOwnerId(UserId userId);
}
