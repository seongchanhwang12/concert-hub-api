package kr.hhplus.be.server.payment.infra;

import kr.hhplus.be.server.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {

    @Query("""
            select p
            from PaymentEntity p
            where p.reservationId = :reservationId
    """)
    Optional<PaymentEntity> findByReservationId(UUID reservationId);


    @Modifying
    @Query(value= """
        INSERT INTO PAYMENT(
            id, 
            idempotency_key, 
            reservation_id, 
            user_id, 
            paid_amount,
            payment_status,
            payment_type,
            paid_at
            )
            VALUES (
                :id, 
                :idempotencyKey, 
                :reservationId, 
                :userId, 
                :paidAmount, 
                :paymentStatus,
                :paymentType,
                :paidAt
            ON CONFLICT (user_id, idempotency_key) DO NOTHING 
    """,nativeQuery = true)
    int trySaveIdempotency(
            @Param("id") UUID id,
            @Param("idempotencyKey") UUID idempotencyKey,
            @Param("reservationId") UUID reservationId,
            @Param("userId") UUID userId,
            @Param("paidAmount") long paidAmount,
            @Param("paymentStatus") String paymentStatus,
            @Param("paymentType") String paymentType,
            @Param("paidAt") LocalDateTime paidAt
    );


    Optional<PaymentEntity> findByUserIdAndIdempotencyKey(UUID value, UUID idempotencyKey);
}
