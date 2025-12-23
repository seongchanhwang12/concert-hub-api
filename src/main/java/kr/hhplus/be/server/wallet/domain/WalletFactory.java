package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.reservation.app.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletFactory {

    private final IdGenerator idGenerator;

    public Wallet create(UserId userId){
        final WalletId walletId = WalletId.of(idGenerator.nextId());
        return Wallet.create(walletId,userId,Point.zero());
    }

    public Wallet empty(UserId userId) {
        WalletId walletId = WalletId.of(idGenerator.nextId());
        return Wallet.empty(walletId,userId);
    }
}
