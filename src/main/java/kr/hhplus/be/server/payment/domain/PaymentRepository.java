package kr.hhplus.be.server.payment.domain;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.payment.infra.PaymentEntity;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    void save(Payment payment);
    Optional<Payment> findByReservationId(UUID uuid);

    Optional<Payment> findById(UUID paymentId);

    boolean trySaveIdempotency(Payment payment);

    Optional<Payment> findByUserIdAndIdempotencyKey(UserId userId, UUID idempotencyKey);

}
