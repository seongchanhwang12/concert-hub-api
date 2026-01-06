package kr.hhplus.be.server.concert.infra.seat.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.SeatStatus;
import kr.hhplus.be.server.concert.infra.seat.jpa.JpaSeat;
import kr.hhplus.be.server.concert.infra.seat.jpa.QJpaSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatQueryRepositoryImpl implements SeatQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<JpaSeat> findAvailableSeats(ScheduleId scheduleId) {
        QJpaSeat s = QJpaSeat.jpaSeat;
        return query.select(s)
                .from(s)
                .where(
                        s.scheduleId.eq(scheduleId.value()),
                        s.status.eq(SeatStatus.AVAILABLE.toString())
                ).orderBy(s.number.asc())
                .fetch();
    }
}
