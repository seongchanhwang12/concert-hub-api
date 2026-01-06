package kr.hhplus.be.server.wallet.domain;

import java.util.UUID;

public record WalletTransactionId(UUID value){

    public static WalletTransactionId of(UUID value) {
        return new WalletTransactionId(value);
    }
}
