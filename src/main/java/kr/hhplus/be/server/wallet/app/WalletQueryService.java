package kr.hhplus.be.server.wallet.app;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.domain.Point;
import kr.hhplus.be.server.wallet.domain.WalletReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletQueryService implements GetPointBalanceUseCase {

    private final WalletReader walletReader;

    @Override
    public Point get(UserId userId) {
        return walletReader.findPointBalanceByOwnerId(userId);
    }
}
