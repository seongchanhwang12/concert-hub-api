package kr.hhplus.be.server.reservation.app;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.common.domain.exception.ApplicationException;
import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.concert.app.schedule.ScheduleQueryService;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.Seat;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.SeatStatus;
import kr.hhplus.be.server.concert.domain.seat.exception.SeatException;
import kr.hhplus.be.server.reservation.domain.Reservation;
import kr.hhplus.be.server.reservation.domain.ReservationFixture;
import kr.hhplus.be.server.reservation.domain.ReservationRepository;
import kr.hhplus.be.server.reservation.domain.ReservationStatus;
import kr.hhplus.be.server.schedule.fixture.ScheduleFixture;
import kr.hhplus.be.server.seat.fixture.SeatFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ReserveSeatUseCaseTest {

    ReserveSeatUseCase reserveSeatUseCase;

    @Mock
    SeatRepository seatRepository;

    @Mock
    ScheduleQueryService scheduleQueryService;

    @Mock
    ReservationRepository reservationRepository;
    IdGenerator idGenerator = new FakeIdGenerator();


    @BeforeEach
    void setBefore(){
        // clock 이 랜덤한 시간을 뱉지 않도록 하기위해
        Clock clock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("UTC"));
        reserveSeatUseCase =  new ReserveSeatUseCase(
                seatRepository,
                reservationRepository,
                scheduleQueryService,
                idGenerator,
                clock);
    }

    /**
     * 검증 :
     *  1. 레파지토리에 좌석 save 함수 실행되어야 한다.
     *  2. 실제 레파지토리 save 에 전달된 예약 객체가 기대값과 일치해야 한다.
     *  3. Seat 의 상태가 Hold 상태로 변경되어야 한다.
     *  4. 예약 성공시 상태가 confirmed 여야한다.
     */
    @Nested
    @DisplayName("좌석이 존재하고 예약 가능한 상태(AVAILABLE) 일 때")
    class WhenSeatIsAvailable{

        Seat seat;
        UserId userId;

        @Captor
        ArgumentCaptor<Reservation> reservationCaptor;

        @BeforeEach
        void init(){
            seat = SeatFixture.createSeat(SeatStatus.AVAILABLE);
            userId = UserId.of(idGenerator.nextId());

            given(seatRepository.find(seat.getId()))
                    .willReturn(Optional.of(seat));
        }

        /**
         * 검증:
         * 1. 예약시 좌석정보(좌석 ID, 좌석 번호, 가격, 예약일, 예약시간, 좌석 등급,
         *
         */
        @Test
        @DisplayName("예약 정보를 저장하고 반환한다.")
        void should_save_and_return_reservation() {
            //given
            Reservation reservation = ReservationFixture.createConfirmedWith(seat.getId(), seat.getScheduleId());
            Schedule schedule = ScheduleFixture.createWithId(ScheduleId.of(1L));
            ReserveSeatCommand cmd = ReserveSeatCommand.of(userId, seat.getId());

            given(reservationRepository.save(any(Reservation.class)))
                    .willReturn(reservation);

            given(scheduleQueryService.findScheduleById(seat.getScheduleId()))
                    .willReturn(schedule);

            //when
            reserveSeatUseCase.reserveSeat(cmd);

            //then
            // 1. Reservation save 인자로 넘어간 값 검증
            then(reservationRepository).should().save(reservationCaptor.capture());
            Reservation savedReservation = reservationCaptor.getValue();

            assertThat(savedReservation.getSeatId()).isEqualTo(seat.getId());
            assertThat(savedReservation.getUserId()).isEqualTo(userId);
            assertThat(savedReservation.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);

            // 2.Seat 상태가 HOLD 로 변경되었는지
            assertThat(seat.getStatus()).isEqualTo(SeatStatus.HOLD);

            // 3. Seat 저장이 호출되었는지
            then(seatRepository).should().save(seat);
        }
    }

    @Nested
    @DisplayName("좌석이 존재하지만 예약 불가능한 상태(HOLD, RESERVED) 일 때")
    class WhenSeatIsNotAvailable{

        UserId userId;

        @BeforeEach
        void init(){
            userId = UserId.of(idGenerator.nextId());
        }

        @Test
        @DisplayName("상태가 HOLD 이면 SeatException 을 던지고 저장되지 않는다.")
        void should_throw_seatException_when_seat_status_is_hold() {
            //given
            Seat seat = SeatFixture.createSeat(SeatStatus.HOLD);
            given(seatRepository.find(seat.getId()))
                    .willReturn(Optional.of(seat));

            //when & then
            assertThatThrownBy(()-> reserveSeatUseCase.reserveSeat(ReserveSeatCommand.of(userId, seat.getId())))
                    .isInstanceOf(SeatException.class);

            // 예약 관련 저장은 없어야 한다.
            then(reservationRepository).shouldHaveNoInteractions();
            then(seatRepository).should(never()).save(any());

        }

        @Test
        @DisplayName("상태가 RESERVED 이면 SeatException 을 던지고 예약이 저장되지 않는다.")
        void should_throw_seatException_when_seat_status_is_reserved(){
            // given
            Seat seat = SeatFixture.createSeat(SeatStatus.RESERVED);
            ReserveSeatCommand reserveSeatCommand = ReserveSeatCommand.of(userId, seat.getId());

            given(seatRepository.find(seat.getId()))
                    .willReturn(Optional.of(seat));

            //when & then
            assertThatThrownBy(()-> reserveSeatUseCase.reserveSeat(reserveSeatCommand))
                    .isInstanceOf(SeatException.class);

            // 예약 관련 저장은 없어야 한다.
            then(reservationRepository).shouldHaveNoInteractions();
            then(seatRepository).should(never()).save(any());
        }

    }

    @Nested
    @DisplayName("좌석이 존재하지 않을 때")
    class WhenSeatDoesNotExists{

        @Test
        @DisplayName("좌석 조회시 NotFoundException 을 던진다.")
        void should_throw_not_found_exception(){
            //given
            SeatId seatId = SeatId.of(1L);
            UserId userId = UserId.of(idGenerator.nextId());
            ReserveSeatCommand reserveSeatCommand = ReserveSeatCommand.of(userId, seatId);

            given(seatRepository.find(seatId)).willReturn(Optional.empty());

            //when & then
            // 1. seatID 로 조회되는 좌석이 없으면, NotFoundException
            assertThatThrownBy(()-> reserveSeatUseCase.reserveSeat(reserveSeatCommand))
                    .isInstanceOf(ApplicationException.class);
            // 2. 예약 관련 저장이 없어야 한다.
            then(reservationRepository).shouldHaveNoInteractions();
            then(seatRepository).should(never()).save(any());
        }
    }


}