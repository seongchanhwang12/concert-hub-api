package kr.hhplus.be.server.wallet.domain;

import kr.hhplus.be.server.reservation.app.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletTransactionFactory {
    private final IdGenerator idGenerator;

    public WalletTransaction createCharge(Wallet wallet, Point chargeAmount, UUID idempotencyKey) {
        final WalletTransactionId walletTransactionId = WalletTransactionId.of(idGenerator.nextId());
        return WalletTransaction.createCharge(
                walletTransactionId,
                wallet,
                chargeAmount,
                idempotencyKey);

    }

    public WalletTransaction createUse(Wallet wallet, UUID idempotencyKey, UUID paymentId) {
        final WalletTransactionId walletTransactionId = WalletTransactionId.of(idGenerator.nextId());
        final Point balanceAfter = wallet.getBalance();
        final TransactionReference reference = new TransactionReference(paymentId.toString());
        return WalletTransaction.createUse(
                walletTransactionId,
                wallet,
                balanceAfter,
                reference,
                idempotencyKey);

    }
}
