package kr.hhplus.be.server.payment.app;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import kr.hhplus.be.server.payment.domain.Payment;
import kr.hhplus.be.server.payment.domain.PaymentStatus;
import kr.hhplus.be.server.payment.domain.PaymentType;
import kr.hhplus.be.server.payment.domain.QueueTokenRepository;
import kr.hhplus.be.server.queue.domain.QueueToken;
import kr.hhplus.be.server.queue.domain.QueueTokenStatus;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.domain.ReservationId;
import kr.hhplus.be.server.reservation.domain.ReservationRepository;
import kr.hhplus.be.server.wallet.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class PointPaymentProcessorTest {

    @InjectMocks
    PointPaymentProcessor pointPaymentProcessor;

    @Mock
    WalletRepository walletRepository;

    @Mock
    WalletTransactionRepository walletTransactionRepository;

    @Mock
    WalletTransactionFactory walletTransactionFactory;

    @Mock
    QueueTokenRepository queueTokenRepository;

    @Mock
    ReservationRepository reservationRepository;

    @Captor
    ArgumentCaptor<Wallet> walletCaptor;

    /**
     * 포인트 결제 처리 테스트
     * - 지갑에서 포인트 차감
     * - 포인트 사용 기록 생성
     * - 결제 기록 생성
     * - 잔액 검증
     */
    @Test
    void payment() {
        //given
        UUID idempotencyKey = UUID.randomUUID();
        UUID tokenValue = UUID.randomUUID();
        UserId userId = UserId.of(UUID.randomUUID());
        ReservationId reservationId = ReservationId.of(UUID.randomUUID());
        long balanceAmount = 10_000L;
        long paymentAmount = 5_000L;
        Point walletBalance = Point.of(balanceAmount);
        Wallet wallet = WalletFixture.createWith(userId, walletBalance);

        Payment payment = Payment.builder()
                .id(UUID.randomUUID())
                .reservationId(reservationId)
                .paidAmount(Money.wons(paymentAmount))
                .status(PaymentStatus.PENDING)
                .type(PaymentType.POINT)
                .paidAt(LocalDateTime.now())
                .build();

        // 포인트 거래 내역 생성
        WalletTransaction useTransaction = WalletTransactionFixture.createUse(wallet, paymentAmount, idempotencyKey, payment.getId().toString());
        QueueToken activeToken = QueueToken.builder()
                .tokenValue(tokenValue)
                .status(QueueTokenStatus.ACTIVE)
                .build();

        // 토큰 조회
        given(queueTokenRepository.findByTokenValue(tokenValue))
                .willReturn(Optional.of(activeToken));

        // 예약 조회
        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .amount(Money.wons(paymentAmount))
                .seatId(SeatId.of(1L))
                .userId(userId)
                .build();
        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));

        // 사용자 포인트 조회
        given(walletRepository.findByOwnerId(payment.getUserId()))
                .willReturn(Optional.of(wallet));

        // 포인트 사용 거래 내역 생성
        given(walletTransactionFactory.createUse(wallet,idempotencyKey,payment.getId()))
                .willReturn(useTransaction);

        // 포인트 사용 기록 생성 성공(멱등) - 최초 요청시
        given(walletTransactionRepository.trySaveIdempotency(useTransaction))
                .willReturn(true);

        //when
        PaymentProcessResult actual = pointPaymentProcessor.pay(payment, idempotencyKey);

        //then
        then(walletTransactionRepository).should().trySaveIdempotency(useTransaction);
        then(walletRepository).should().save(walletCaptor.capture());
        Wallet capturedWallet = walletCaptor.getValue();

        assertThat(capturedWallet.getBalance()).isEqualTo(walletBalance.minus(Point.of(paymentAmount)) );

        then(walletTransactionRepository).should(never()).findByOwnerIdAndIdempotencyKey(userId, idempotencyKey);
        assertThat(actual).isNotNull();
        // 저장 실패시 기존 조정조회 로직은 실행되지 않는다


    }


}