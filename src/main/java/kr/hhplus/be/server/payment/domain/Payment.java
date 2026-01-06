package kr.hhplus.be.server.payment.domain;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.reservation.domain.ReservationId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Payment {
    private final UUID id;
    private final UUID idempotencyKey;
    private final ReservationId reservationId;
    private final UserId userId;
    private Money paidAmount;
    private PaymentStatus status;
    private PaymentType type;
    private LocalDateTime paidAt;

    public static Payment createSuccess(UUID id,
                                        Money paidAmount,
                                        PaymentType paymentType,
                                        ReservationId reservationId,
                                        UserId userId,LocalDateTime now) {
        return Payment.builder()
                .id(id)
                .paidAmount(paidAmount)
                .type(paymentType)
                .reservationId(reservationId)
                .userId(userId)
                .paidAt(now)
                .build();
    }

    public static Payment createPending(
            UUID id,
            UUID idempotencyKey,
            ReservationId reservationId,
            UserId userId,
            Money paidAmount,
            PaymentType paymentType,
            LocalDateTime now)
    {
        return Payment.builder()
                .id(id)
                .idempotencyKey(idempotencyKey)
                .reservationId(reservationId)
                .userId(userId)
                .paidAmount(paidAmount)
                .status(PaymentStatus.PENDING)
                .type(paymentType)
                .paidAt(now)
                .build();
    }

    public void failure(LocalDateTime paidAt) {
        this.status = PaymentStatus.FAILED;
        this.paidAt = paidAt;
    }

    public void success(LocalDateTime paidAt) {
        this.status = PaymentStatus.SUCCESS;
        this.paidAt = paidAt;
    }

    public boolean isSuccess() {
        return status == PaymentStatus.SUCCESS;
    }
}
