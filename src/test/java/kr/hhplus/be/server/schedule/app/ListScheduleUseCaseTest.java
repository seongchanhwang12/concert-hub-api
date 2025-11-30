package kr.hhplus.be.server.schedule.app;

import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.concert.domain.ConcertId;
import kr.hhplus.be.server.common.UUIDGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListScheduleUseCaseTest {

    @Mock
    ListScheduleUseCase listScheduleUseCase;
    UUIDGenerator fakeIdGenerator = new FakeIdGenerator();

    @DisplayName("특정 콘서트의 케줄 리스트 조회시, Schedule 리스트를 반환한다.")
    @Test
    void given_concertId_when_find_scheduleList_should_return_schedule_list() {
        //given
        long id = 1L;
        ConcertId concertId = new ConcertId(id);
        //ListScheduleCommand command = new ListScheduleCommand(concertId);

        //when
        listScheduleUseCase.listSchedules();

        //then
        
    }

}