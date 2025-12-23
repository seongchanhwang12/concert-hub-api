package kr.hhplus.be.server.wallet.app.dto;

import kr.hhplus.be.server.wallet.domain.Point;
import kr.hhplus.be.server.wallet.domain.WalletTransaction;
import kr.hhplus.be.server.wallet.domain.WalletTransactionId;

public record ChargePointResult(WalletTransactionId transactionId, Point chargedPoint, Point balanceAfter) {

    public static ChargePointResult from(WalletTransaction walletTransaction) {
        return new ChargePointResult(
                walletTransaction.getId(),
                walletTransaction.getPointAmount(),
                walletTransaction.getBalanceAfter());
    }

}
