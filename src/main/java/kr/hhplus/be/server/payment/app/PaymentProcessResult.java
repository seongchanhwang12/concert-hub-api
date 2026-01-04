package kr.hhplus.be.server.payment.app;

import kr.hhplus.be.server.payment.domain.Payment;

import java.util.UUID;

public record PaymentProcessResult(UUID paymentId) {

    public static PaymentProcessResult from(Payment payment) {
        return null;
    }
}
