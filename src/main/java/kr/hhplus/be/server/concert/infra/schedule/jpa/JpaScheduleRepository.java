package kr.hhplus.be.server.concert.infra.schedule.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaScheduleRepository extends JpaRepository<JpaSchedule, Long> {
    List<JpaSchedule> findAllByConcertId(Long concertId);
}
