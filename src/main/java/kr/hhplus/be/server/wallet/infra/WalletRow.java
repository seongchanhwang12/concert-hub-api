package kr.hhplus.be.server.wallet.infra;

import java.util.UUID;

public interface WalletRow {
    UUID getId();
    UUID getOwnerId();
    Long getPoint();
}
