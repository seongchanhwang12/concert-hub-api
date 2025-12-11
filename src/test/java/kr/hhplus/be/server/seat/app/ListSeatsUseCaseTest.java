package kr.hhplus.be.server.seat.app;

import kr.hhplus.be.server.concert.app.seat.ListSeatsUseCase;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.SeatStatus;
import kr.hhplus.be.server.concert.domain.seat.Seats;
import kr.hhplus.be.server.seat.fixture.SeatFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ListSeatsUseCaseTest {

    @InjectMocks
    ListSeatsUseCase listScheduleSeatsUseCase;

    @Mock
    SeatRepository seatRepository;

    @DisplayName("주어진 공연 일정에 대해 사용 가능한 좌석 목록을 반환한다.")
    @Test
    void given_show_date_when_query_available_seats_then_return_seat_list() {
        //given
        long scheduleId = 1L;
        Seats seats = SeatFixture.createSeats(SeatStatus.AVAILABLE);

        given(seatRepository.findAvailableSeats(any(ScheduleId.class)))
                .willReturn(seats);

        //when
        Seats actual = listScheduleSeatsUseCase.listSeats(ScheduleId.of(scheduleId));

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.values().size()).isEqualTo(seats.values().size());

    }

    @DisplayName("주어진 공연 일정에 대해 이용가능한 좌석이 없으면 빈 좌석 목록을 반환한다.")
    @Test
    void given_repository_error_when_query_available_seats_then_return_empty_seats() {
        //given
        ScheduleId scheduleId = ScheduleId.of(1L);

        given(seatRepository.findAvailableSeats(any(ScheduleId.class)))
                .willReturn(Seats.empty());

        //when
        Seats actual = listScheduleSeatsUseCase.listSeats(scheduleId);

        //then
        then(seatRepository).should(times(1))
                .findAvailableSeats(scheduleId);

        assertThat(actual).isNotNull();
        assertThat(actual.values()).isEmpty();
        assertThat(actual.values()).allSatisfy(seat ->{
            assertThat(seat.getScheduleId()).isEqualTo(scheduleId);
            assertThat(seat.getStatus()).isEqualTo(SeatStatus.AVAILABLE);
        });
    }


}