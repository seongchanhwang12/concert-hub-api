package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.reservation.app.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletTransactionFactory {
    private final IdGenerator idGenerator;

    public WalletTransaction createCharge(Wallet wallet, UUID idempotencyKey) {
        final WalletTransactionId walletTransactionId = WalletTransactionId.of(idGenerator.nextId());
        final Point balanceAfter = wallet.getPoint();

        return WalletTransaction.createCharge(
                walletTransactionId,
                wallet.getId(),
                wallet.getOwnerId(),
                wallet.getPoint(),
                balanceAfter,
                idempotencyKey);

    }
}
