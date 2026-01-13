package kr.hhplus.be.server.payment.domain;

import java.time.Clock;
import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.reservation.app.IdGenerator;
import kr.hhplus.be.server.reservation.domain.ReservationId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentFactory {

    private final IdGenerator idGenerator;
    private final Clock clock;

    public Payment createPending(UUID IdempotencyKey, ReservationId reservationId, Money paidAmount, UserId userId) {
        UUID paymentId = idGenerator.nextId();
        return Payment.createPending(paymentId
                , IdempotencyKey
                , reservationId
                , userId
                , paidAmount
                , PaymentType.POINT
                , LocalDateTime.now());

    }

    public Payment createSuccess(ReservationId reservationId, Money reservationAmount, UserId userId,UUID idempotencyKey) {
        UUID paymentId = idGenerator.nextId();
        LocalDateTime now = LocalDateTime.now(clock);
        return Payment.createSuccess(
                paymentId,
                idempotencyKey,
                reservationAmount,
                PaymentType.POINT,
                reservationId,
                userId,
                now);
    }
}
