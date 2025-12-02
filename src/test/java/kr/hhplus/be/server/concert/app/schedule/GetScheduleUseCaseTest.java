package kr.hhplus.be.server.concert.app.schedule;

import kr.hhplus.be.server.common.app.NotFoundException;
import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import kr.hhplus.be.server.schedule.fixture.ScheduleFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetScheduleUseCaseTest {

    @InjectMocks
    GetScheduleUseCase getScheduleUseCase;

    @Mock
    ScheduleRepository scheduleRepository;

    /**
     * 검증 :
     * - 특정 scheduleId 로 일정 조회시
     */
    @Test
    @DisplayName("특정 scheduleId 로 일정 조회시 콘서트 스케줄 상세 정보를 조회한다.")
    void given_scheduleId_when_getSchedule_then_return_ScheduleDetail() {
        //given
        long scheduleIdValue = 1L;
        long concertIdValue = 1L;

        ScheduleId scheduleId = ScheduleId.of(scheduleIdValue);
        ConcertId concertId = ConcertId.of(concertIdValue);
        Schedule schedule = ScheduleFixture.createScheduleWithId(concertId, scheduleId);

        given(scheduleRepository.findById(scheduleId))
                .willReturn(Optional.of(schedule));

        //when
        Schedule scheduleDetail = getScheduleUseCase.getScheduleDetail(scheduleId);

        //then
        assertThat(scheduleDetail).isNotNull();
        assertThat(scheduleDetail.getConcertId()).isEqualTo(concertId);
        assertThat(scheduleDetail.getId()).isEqualTo(scheduleId);

    }
    @Test
    @DisplayName("특정 스케줄 조회시, 스케줄 정보가 없으면, NotFoundException 을 던진다.")
    void given_when_then () {
        //given
        long scheduleIdValue = 1L;

        ScheduleId scheduleId = ScheduleId.of(scheduleIdValue);

        //when & then
        assertThatThrownBy(()-> getScheduleUseCase.getScheduleDetail(scheduleId))
                .isInstanceOf(NotFoundException.class);

    }

}