package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.common.domain.UserId;

import java.util.UUID;

public class WalletFixture {

    public static Wallet createWith(UserId userId, Point amount) {
        WalletId walletId = WalletId.of(UUID.randomUUID());
        return Wallet.create(walletId, userId, amount);
    }
}