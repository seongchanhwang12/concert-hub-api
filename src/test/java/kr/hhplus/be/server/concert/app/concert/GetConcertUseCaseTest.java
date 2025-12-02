package kr.hhplus.be.server.concert.app.concert;

import kr.hhplus.be.server.common.app.NotFoundException;
import kr.hhplus.be.server.concert.app.concert.exception.GetConcertUseCase;
import kr.hhplus.be.server.concert.domain.concert.Concert;
import kr.hhplus.be.server.concert.domain.concert.ConcertDetail;
import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.concert.ConcertRepository;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import kr.hhplus.be.server.concert.domain.schedule.Schedules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kr.hhplus.be.server.concert.ConcertFixture.createConcert;
import static kr.hhplus.be.server.schedule.fixture.ScheduleFixture.createSchedulesWithId;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetConcertUseCaseTest {

    @InjectMocks
    GetConcertUseCase getConcertUseCase;

    @Mock
    ConcertRepository concertRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    /**
     * 검증 :
     *  - 올바른 ConcertId로 조회시, Concert가 null이면 안된다.
     *  - ConcertId와 조회된 Concert의 id 값이 같아야 한다.
     */
    @DisplayName("올바른 concertId 를 받으면, 콘서트 정보를 반환한다.")
    @Test
    void getConcert_success() {
        //given
        // Concert 데이터 준비
        long concertIdValue = 1L;
        ConcertId concertId = new ConcertId(concertIdValue);
        Concert concert = createConcert(concertId);

        // Schedule 데이터 준비
        long scheduleIdValue = 1L;
        ScheduleId scheduleId = ScheduleId.of(scheduleIdValue);
        Schedules schedules = createSchedulesWithId(concertId, scheduleId);

        // 콘서트 조회
        given(concertRepository.findConcertByConcertId(concertId))
                .willReturn(Optional.of(concert));

        // 스케줄 조회
        given(scheduleRepository.findSchedulesByConcertId(concertId))
                .willReturn(schedules);

        //when
        ConcertDetail actual = getConcertUseCase.getConcertDetail(concertId);

        //then
        assertThat(actual).isNotNull();
        assertThat(actual.concert()).isEqualTo(concert);
        assertThat(actual.schedules()).isEqualTo(schedules);
    }

    /**
     * 검증 :
     *  - ConcertId로 값이 조회되지 않는 경우 NotFoundException을 throw 한다.
     */
    @DisplayName("concertId가 올바르지 않으면, NotFoundException 을 반환한다")
    @Test
    void should_throw_exception_when_invalid_concert_id() {
        //given
        long id = 1L;
        ConcertId concertId = ConcertId.of(id);

        //when & then
        assertThatThrownBy(() -> getConcertUseCase.getConcertDetail(concertId))
                .isInstanceOf(NotFoundException.class);

    }



}