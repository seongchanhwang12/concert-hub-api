package kr.hhplus.be.server.wallet.domain;

import java.util.UUID;

public record WalletId(UUID value) {

    public static WalletId of(UUID id) {
        return new WalletId(id);
    }
}
