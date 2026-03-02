package kr.hhplus.be.server.payment.app;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.concert.domain.seat.Seat;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.SeatStatus;
import kr.hhplus.be.server.payment.domain.*;
import kr.hhplus.be.server.queue.domain.QueueToken;
import kr.hhplus.be.server.queue.domain.QueueTokenStatus;
import kr.hhplus.be.server.reservation.app.IdGenerator;
import kr.hhplus.be.server.reservation.domain.*;
import kr.hhplus.be.server.seat.fixture.SeatFixture;
import kr.hhplus.be.server.wallet.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Clock;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PayForReservationUseCaseTest {

    @InjectMocks
    PayForReservationUseCase payForReservationUseCase;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    WalletRepository walletRepository;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    WalletTransactionRepository walletTransactionRepository;

    @Mock
    WalletTransactionFactory walletTransactionFactory;

    @Mock
    SeatRepository seatRepository;

    @Mock
    PaymentFactory paymentFactory;

    @Mock QueueTokenRepository queueTokenRepository;

    Clock clock;

    @BeforeEach void setUp(){
        Instant parse = Instant.parse("2025-01-01T10:00:00Z");
        clock = Clock.fixed(parse, ZoneId.of("UTC"));

        payForReservationUseCase = new PayForReservationUseCase(walletRepository,
                reservationRepository,
                walletTransactionFactory,
                paymentFactory,
                walletTransactionRepository,
                paymentRepository,
                queueTokenRepository,
                seatRepository,
                clock);
    }

    IdGenerator idGenerator = new FakeIdGenerator();

    @Captor
    ArgumentCaptor<Wallet> walletCaptor;

    @Captor
    ArgumentCaptor<Payment> paymentCaptor;

    @Captor
    ArgumentCaptor<Seat> seatCaptor;

    // 흐름 : 예약 완료 -> 결졔 요청 -> 포인트 차감 -> 결제 내역 생성
    // 1. 포인트 차감
    // - 예약 내역 조회
    // - 지갑 조회
    // - 포인트 차감 (예약금만큼) -> 차감 내역 저장
    // - 원장 생성
    // - 결재 내역 생성 -> 반환
    @Test
    @DisplayName("예약을 조회하고, 예약 금액 결제시 포인트 지갑에서 예약 금액만큼의 포인트가 차감된다.")
    void Given_reservation_When_pay_reservation_fee_Then_return_paymentResult() {
        //given
        UUID idempotencyKey = idGenerator.nextId();
        UUID reservationIdValue = idGenerator.nextId();
        ReservationId reservationId = ReservationId.of(reservationIdValue);
        UUID tokenId = idGenerator.nextId();
        WalletTransactionId walletTransactionId = WalletTransactionId.of(UUID.randomUUID());

        UserId userId = UserId.of(idGenerator.nextId());
        Money reservationAmount = Money.wons(10_000L);
        Point walletPointBalance = Point.of(10_000L);
        Point usePoint = Point.of(reservationAmount.value());

        QueueToken queueToken = QueueToken.builder()
                .id(1L)
                .status(QueueTokenStatus.ACTIVE)
                .build();

        Reservation reservation = ReservationFixture.createConfirmedWith(reservationAmount,userId);


        Payment paymentSuccess = Payment.createSuccess(
                idGenerator.nextId(),
                idempotencyKey,
                reservation.getAmount(),
                PaymentType.POINT,
                reservation.getId(),
                userId,
                LocalDateTime.now(clock));

        Wallet wallet = WalletFixture.createWith(userId, walletPointBalance);
        WalletTransaction useTx = WalletTransaction.createUse(walletTransactionId, wallet, usePoint, new TransactionReference(paymentSuccess.getId().toString()), idempotencyKey );

        SeatId seatId = SeatId.of(1L);
        LocalDateTime holdAt = LocalDateTime.now(clock);
        Seat heldSeat = SeatFixture.createHoldSeat(seatId, SeatStatus.HOLD, holdAt);

        given(queueTokenRepository.findByTokenValue(tokenId))
                .willReturn(Optional.of(queueToken));

        // 홀드 상태의 좌석 조회
        /*
        given(paymentFactory.createPending(eq(idempotencyKey),any(ReservationId.class), eq(reservationAmount), eq(userId)))
                .willReturn(paymentSuccess);
        */
        given(seatRepository.find(reservation.getSeatId()))
                .willReturn(Optional.of(heldSeat));

        given(reservationRepository.findById(reservationId))
                .willReturn(Optional.of(reservation));

        given(paymentRepository.findByUserIdAndIdempotencyKey(userId, idempotencyKey))
                .willReturn(Optional.of(paymentSuccess));

        given(walletRepository.findByOwnerId(userId))
                .willReturn(Optional.of(wallet));

        given(walletTransactionFactory.createUse(wallet, idempotencyKey, paymentSuccess.getId())).willReturn(useTx);

        // 멱등 저장 성공
        given(walletTransactionRepository.trySaveIdempotency(any(WalletTransaction.class)))
                .willReturn(true);

        given(paymentFactory.createSuccess(any(ReservationId.class), eq(reservationAmount), eq(userId),eq(idempotencyKey)))
                .willReturn(paymentSuccess);

        // 결제 명령
        PayReservationCommand cmd = new PayReservationCommand(idempotencyKey, reservationId.value(), tokenId, userId);

        //when
        PaymentResult actual = payForReservationUseCase.pay(cmd);

        //then
        then(seatRepository).should().save(seatCaptor.capture());
        Seat caputuredSeat = seatCaptor.getValue();
        assertThat(caputuredSeat.getStatus()).isEqualTo(SeatStatus.RESERVED);

        then(walletRepository).should().save(walletCaptor.capture());
        Wallet capturedWallet = walletCaptor.getValue();
        assertThat(capturedWallet.getBalance()).isEqualTo(walletPointBalance.minus(usePoint));

        then(paymentRepository).should().save(paymentCaptor.capture());
        Payment capturedPayment = paymentCaptor.getValue();
        assertThat(capturedPayment.getPaidAmount()).isEqualTo(reservation.getAmount());

        // 멱등 실패시에만 호출
        // then(walletTransactionRepository).should().findByOwnerIdAndIdempotencyKey(userId, idempotencyKey);
        // assertThat(actual.paidAmount()).isEqualTo(reservation.getAmount());
    }

}