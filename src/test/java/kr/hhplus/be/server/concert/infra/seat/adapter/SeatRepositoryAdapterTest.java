package kr.hhplus.be.server.concert.infra.seat.adapter;

import jakarta.persistence.EntityManager;
import kr.hhplus.be.server.PostgresTestcontainersConfig;
import kr.hhplus.be.server.concert.domain.concert.ConcertId;
import kr.hhplus.be.server.concert.domain.schedule.Schedule;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.concert.domain.seat.SeatRepository;
import kr.hhplus.be.server.concert.domain.seat.SeatStatus;
import kr.hhplus.be.server.concert.domain.seat.Seats;
import kr.hhplus.be.server.concert.infra.schedule.jpa.JpaSchedule;
import kr.hhplus.be.server.concert.infra.schedule.jpa.JpaScheduleFixture;
import kr.hhplus.be.server.concert.infra.seat.jpa.JpaSeat;
import kr.hhplus.be.server.concert.infra.seat.jpa.JpaSeatFixture;
import kr.hhplus.be.server.concert.infra.seat.mapping.SeatEntityMapperImpl;
import kr.hhplus.be.server.concert.infra.seat.query.SeatQueryRepositoryImpl;
import kr.hhplus.be.server.config.query.QueryDslConfig;
import kr.hhplus.be.server.schedule.fixture.ScheduleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class,
        SeatRepositoryAdapter.class,
        SeatQueryRepositoryImpl.class,
        PostgresTestcontainersConfig.class,
        SeatEntityMapperImpl.class})
class SeatRepositoryAdapterTest {

    @Autowired
    EntityManager em;

    @Autowired
    SeatRepository seatRepository;

    /**
     * given : 스케줄 엔티티와 사용 가능한 좌석, 예약 중인 좌석이 존재할 때
     * when : 해당 스케줄로 사용 가능한 좌석들을 조회하면
     * then : 사용 가능한 좌석들만 반환된다.
     *
     * 스케줄 ID와 좌석  상태를 기준으로 사용 가능한 좌석만 필터링하는 쿼리 동작을 검증한다.
     */
    @Test
    @DisplayName("스케줄을 받으면 사용 가능한 좌석들만 반환한다.")
    void given_schedule_when_findAvailableSeats_then_only_available_seats_returned() {
        //given
        ConcertId concertId = new ConcertId(1L);
        JpaSchedule jpaSchedule = JpaScheduleFixture.create(concertId);
        em.persist(jpaSchedule);

        JpaSeat available = JpaSeatFixture.create(jpaSchedule.getId(), SeatStatus.AVAILABLE);
        JpaSeat held = JpaSeatFixture.create(jpaSchedule.getId(), SeatStatus.HOLD);

        em.persist(available);
        em.persist(held);

        // 영속성 컨텍스트를 비워 1차 캐시 영향 제거
        em.flush();
        em.clear();

        //when
        Seats actual = seatRepository.findAvailableSeats(ScheduleId.of(jpaSchedule.getId()));

        //then
        assertThat(jpaSchedule.getId()).isNotNull(); // 스케줄 ID가 정상적으로 생성되었는지 확인

        assertThat(actual.values())
                .hasSize(1)
                .allSatisfy(seat -> assertThat(seat.getStatus()).isEqualTo(SeatStatus.AVAILABLE));


    }

}