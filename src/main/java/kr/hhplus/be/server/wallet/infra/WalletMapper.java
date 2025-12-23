package kr.hhplus.be.server.wallet.infra;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.domain.Point;
import kr.hhplus.be.server.wallet.domain.Wallet;
import kr.hhplus.be.server.wallet.domain.WalletId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WalletMapper {

    public Wallet toDomain(JpaWallet entity){
        return Wallet.of(
                WalletId.of(entity.getId()),
                UserId.of(entity.getOwnerId()),
                Point.of(entity.getPoint()));
    }

    public Wallet toDomain(WalletRow walletRow){
        return Wallet.of(
            WalletId.of(walletRow.getId()),
            UserId.of(walletRow.getOwnerId()),
            Point.of(walletRow.getPoint()));
    }


}
