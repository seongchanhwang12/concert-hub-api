package kr.hhplus.be.server.payment.app;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.PaymentStatus;
import kr.hhplus.be.server.payment.domain.PaymentType;
import kr.hhplus.be.server.wallet.domain.WalletTransaction;

import java.util.UUID;

public record PaymentResult(
        UUID PaymentId,
        Money paidAmount,
        PaymentStatus paymentStatus,
        PaymentType paymentType)
{
    public static PaymentResult from(Payment payment) {
        return new PaymentResult(payment.getId(), payment.getPaidAmount(), payment.getStatus(), payment.getType());
    }

    public static PaymentResult of(WalletTransaction walletTransaction) {
        return null;
    }
}
