package kr.hhplus.be.server.payment.infra;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaAdapter;

    @Override
    public void save(Payment payment) {
        paymentJpaAdapter.save(PaymentEntity.from(payment));
    }

    @Override
    public Optional<Payment> findByReservationId(UUID reservationId) {
        return paymentJpaAdapter.findByReservationId(reservationId)
                .map(PaymentEntity::toDomain);
    }

    @Override
    public Optional<Payment> findById(UUID paymentId) {
        return paymentJpaAdapter.findById(paymentId).map(PaymentEntity::toDomain);
    }

    @Override
    public boolean trySaveIdempotency(Payment payment) {
        return paymentJpaAdapter.trySaveIdempotency(
                payment.getId(),
                payment.getIdempotencyKey(),
                payment.getReservationId().value(),
                payment.getUserId().value(),
                payment.getPaidAmount().value(),
                payment.getStatus().name(),
                payment.getType().name(),
                payment.getPaidAt()) == 1;
    }

    @Override
    public Optional<Payment> findByUserIdAndIdempotencyKey(UserId userId, UUID idempotencyKey) {
        return paymentJpaAdapter.findByUserIdAndIdempotencyKey(userId.value(),idempotencyKey)
                .map(PaymentEntity::toDomain);
    }

}
