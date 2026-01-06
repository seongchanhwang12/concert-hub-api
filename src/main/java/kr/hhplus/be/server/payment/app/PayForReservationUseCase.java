package kr.hhplus.be.server.payment.app;

import kr.hhplus.be.server.common.app.ApiErrorCode;
import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.common.domain.exception.DomainException;
import kr.hhplus.be.server.concert.domain.seat.Seat;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.exception.SeatErrorCode;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.PaymentFactory;
import kr.hhplus.be.server.payment.domain.PaymentRepository;
import kr.hhplus.be.server.payment.domain.QueueTokenRepository;
import kr.hhplus.be.server.queue.domain.QueueToken;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.domain.ReservationErrorCode;
import kr.hhplus.be.server.reservation.domain.ReservationId;
import kr.hhplus.be.server.reservation.domain.ReservationRepository;
import kr.hhplus.be.server.wallet.app.WalletErrorCode;
import kr.hhplus.be.server.wallet.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayForReservationUseCase {
    private final WalletRepository walletRepository;
    private final ReservationRepository reservationRepository;
    private final WalletTransactionFactory walletTransactionFactory;
    private final PaymentFactory paymentFactory;
    private final WalletTransactionRepository walletTransactionRepository;
    private final PaymentRepository paymentRepository;
    private final QueueTokenRepository queueTokenRepository;
    private final SeatRepository seatRepository;
    private final Clock clock;


    private QueueToken getQueueTokenIfActive(UUID tokenId){
        return queueTokenRepository.findById(tokenId)
                .filter(QueueToken::isActive)
                .orElseThrow(()-> new IllegalStateException("token is expired"));
    }

    // 예약 조회
    private Reservation getReservation(UUID reservationId){
        return reservationRepository.findById(reservationId)
                .orElseThrow(()-> new DomainException(ReservationErrorCode.NOT_FOUND, "reservationId [" + reservationId + "] not found" ));
    }

    // 좌석 홀드를 예약 완료 상태로 변경
    private Seat getHoldSeat(SeatId seatId){
        return seatRepository.find(seatId)
                .orElseThrow(() -> new DomainException(SeatErrorCode.NOT_FOUND, "seatId [" + seatId + "] not found" ));
    }

    // 포인트 지갑 조회
    private Wallet getWallet(UserId userId){
        return walletRepository.findByOwnerId(userId)
                .orElseThrow(() -> new DomainException(WalletErrorCode.NOT_FOUND, "userId [" + userId + "] not found"));
    }

    private boolean tryUsePointIdempotency(Payment payment, Wallet wallet){
        WalletTransaction use = walletTransactionFactory.createUse(wallet, payment.getIdempotencyKey(), payment.getId());
        boolean first = walletTransactionRepository.trySaveIdempotency(use);
        if(!first) return false;

        wallet.pay(payment.getPaidAmount());
        walletRepository.save(wallet);
        return true;
    }


    private Payment payIdempotency(UUID idempotencyKey, Reservation reservation){
        Payment payment = paymentFactory.createPending(idempotencyKey, reservation.getId(), reservation.getAmount(), reservation.getUserId());
        boolean first = paymentRepository.trySaveIdempotency(payment);

        if(first){
            return payment;
        }

        return paymentRepository.findByUserIdAndIdempotencyKey(reservation.getUserId(), idempotencyKey)
                .orElseThrow(() -> new DomainException(PaymentErrorCode.NOT_FOUND, "idempotencyKey [" + idempotencyKey + "] not found"));
    }

    /**
     * 결제시 포인트 차감
     * 멱등 보장
     * @param cmd
     * @return
     */
    @Transactional
    public PaymentResult pay(PayReservationCommand cmd) {

        QueueToken queueToken = getQueueTokenIfActive(cmd.tokenId());

        Reservation reservation = getReservation(cmd.reservationId());
        reservation.assertOwnedBy(cmd.userId());

        Seat seat = getHoldSeat(reservation.getSeatId());
        seat.assertHoldAlive(clock);

        Wallet wallet = getWallet(cmd.userId());

        LocalDateTime now = LocalDateTime.now(clock);

        Payment payment = payIdempotency(cmd.idempotencyKey(), reservation);

        if (payment.isSuccess()) {
            return PaymentResult.from(payment);
        }

        boolean debited = tryUsePointIdempotency(payment, wallet);

        if (!debited) {
            // 이미 차감이 진행된 요청일 수 있으니 최신 상태 재확인
            Payment latest = paymentRepository.findByUserIdAndIdempotencyKey(reservation.getUserId(), cmd.idempotencyKey())
                    .orElseThrow(() -> new DomainException(PaymentErrorCode.NOT_FOUND, "idempotencyKey [" + cmd.idempotencyKey() + "] not found"));
            if (latest.isSuccess()) {
                return PaymentResult.from(latest);
            }
            payment = latest; // pending이면 이번 요청이 이어서 success 전이 수행
        }

        payment.success(now);
        paymentRepository.save(payment);

        queueToken.expire();
        queueTokenRepository.save(queueToken);

        seat.reserve(now);
        seatRepository.save(seat);

        return PaymentResult.from(payment);
    }
}
