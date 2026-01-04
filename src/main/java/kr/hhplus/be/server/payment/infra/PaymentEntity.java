package kr.hhplus.be.server.payment.infra;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.PaymentStatus;
import kr.hhplus.be.server.payment.domain.PaymentType;
import kr.hhplus.be.server.reservation.domain.ReservationId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    private UUID id;
    private UUID idempotencyKey;
    private UUID reservationId;
    private UUID userId;
    private long paidAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    private LocalDateTime paidAt;


    public static PaymentEntity from(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId())
                .reservationId(payment.getReservationId().value())
                .userId(payment.getUserId().value())
                .paidAmount(payment.getPaidAmount().value())
                .status(payment.getStatus())
                .paymentType(payment.getType())
                .paidAt(payment.getPaidAt())
                .build();

    }

    public Payment toDomain() {
        return Payment.builder()
                .id(id)
                .reservationId(ReservationId.of(reservationId))
                .userId(UserId.of(userId))
                .paidAmount(Money.wons(paidAmount))
                .status(status)
                .type(paymentType)
                .paidAt(paidAt)
                .build();
    }
}
