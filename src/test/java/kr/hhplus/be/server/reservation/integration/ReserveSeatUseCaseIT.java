package kr.hhplus.be.server.reservation.integration;

import kr.hhplus.be.server.PostgresTestcontainersConfig;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import kr.hhplus.be.server.concert.domain.seat.Seat;
import kr.hhplus.be.server.concert.domain.seat.SeatId;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.SeatStatus;
import kr.hhplus.be.server.reservation.app.IdGenerator;
import kr.hhplus.be.server.reservation.app.ReserveSeatCommand;
import kr.hhplus.be.server.reservation.app.ReserveSeatUseCase;
import kr.hhplus.be.server.reservation.domain.ReservationSeat;
import kr.hhplus.be.server.reservation.domain.ReservationStatus;
import kr.hhplus.be.server.schedule.fixture.ScheduleFixture;
import kr.hhplus.be.server.seat.fixture.SeatFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PostgresTestcontainersConfig.class})
public class ReserveSeatUseCaseIT {

    @Autowired
    ReserveSeatUseCase reserveSeatUseCase;

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    IdGenerator uuidGenerator = new FakeIdGenerator();
    UserId userId;
    Seat seat;

    @BeforeEach
    void setUp(){
        seat = seatRepository.save(SeatFixture.create(SeatStatus.AVAILABLE));
        scheduleRepository.save(ScheduleFixture.createWith(ConcertId.of(1L)));
        userId = UserId.of(uuidGenerator.nextId());
    }


    /**
     *
     */
    @Test
    @DisplayName("좌석 예약_성공 테스트 ")
    void seat_reservation_success(){
        //given
        ReserveSeatCommand reserveSeatCommand = ReserveSeatCommand.of(userId,seat.getId());

        //when
        ReservationSeat actual = reserveSeatUseCase.reserveSeat(reserveSeatCommand);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.reservation().getUserId()).isEqualTo(userId);
        assertThat(actual.reservation().getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(actual.reservation().getSeatId()).isEqualTo(seat.getId());
    }

    /**
     * 동시성 테스트 진행
     * 검증 :
     * - 동시 50명이 좌석 예약시 최초 1건만 예약에 성공
     * - 그 외 49명은 예약 실패
     *
     * @throws InterruptedException
     */
    @DisplayName("좌석 예매 동시성 테스트")
    @Test
    void seat_reservation_concurrency_test () throws InterruptedException {
        //given
        ReserveSeatCommand reserveSeatCommand = ReserveSeatCommand.of(userId,seat.getId());

        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    reserveSeatUseCase.reserveSeat(reserveSeatCommand);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    log.error(e.getMessage(), e);
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        finishLatch.await();

        //then
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(threadCount-1);


    }

}
