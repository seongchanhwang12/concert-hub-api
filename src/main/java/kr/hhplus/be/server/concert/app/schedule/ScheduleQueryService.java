package kr.hhplus.be.server.concert.app.schedule;

import kr.hhplus.be.server.common.domain.exception.ApplicationException;
import kr.hhplus.be.server.concert.app.schedule.exception.ScheduleErrorCode;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleQueryService {

    private final ScheduleRepository scheduleRepository;

    @Transactional(readOnly = true)
    public Schedule findScheduleById(ScheduleId scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new ApplicationException(
                        ScheduleErrorCode.NOT_FOUND,
                        "Schedule not found. id = " +  scheduleId.value()) );
    }
}
