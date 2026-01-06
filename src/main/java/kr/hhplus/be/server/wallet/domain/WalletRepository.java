package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.common.domain.UserId;

import java.util.Optional;

public interface WalletRepository {
    Wallet save(Wallet wallet);

    Optional<Wallet> findByOwnerId(UserId userId);

    Wallet saveAndFlush(Wallet wallet);

    Wallet upsertAndReturn(Wallet wallet);
}
