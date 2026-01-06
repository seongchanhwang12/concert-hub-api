package kr.hhplus.be.server.concert.app.schedule;

import kr.hhplus.be.server.common.domain.exception.ApplicationException;
import kr.hhplus.be.server.concert.app.schedule.exception.ScheduleErrorCode;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    public Schedule getScheduleDetail(ScheduleId scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new ApplicationException(
                        ScheduleErrorCode.NOT_FOUND,
                        "Schedule not found. id = " +  scheduleId.value()) );
    }
}
