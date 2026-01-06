package kr.hhplus.be.server.concert.infra.concert.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaConcertRepository extends JpaRepository<JpaConcert, Long> {
}
