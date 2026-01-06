package kr.hhplus.be.server.concert.integration;

import kr.hhplus.be.server.PostgresTestcontainersConfig;
import kr.hhplus.be.server.concert.ConcertFixture;
import kr.hhplus.be.server.concert.app.concert.exception.GetConcertUseCase;
import kr.hhplus.be.server.concert.domain.concert.Concert;
import kr.hhplus.be.server.concert.domain.concert.ConcertDetail;
import kr.hhplus.be.server.concert.domain.concert.ConcertRepository;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleRepository;
import kr.hhplus.be.server.schedule.fixture.ScheduleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PostgresTestcontainersConfig.class})
class GetConcertUseCaseIT {

    @Autowired
    GetConcertUseCase getConcertUseCase;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @DisplayName("유효한 ID로 조회하면, 콘서트를 반환한다.")
    @Test
    void use_case_is_not_null() {
        //given
        Concert concert = ConcertFixture.createConcert();

        // 콘서트 저장
        Concert savedConcert = concertRepository.save(concert);

        // 스케줄 저장
        scheduleRepository.saveAll(
                ScheduleFixture.createListSchedule(savedConcert.getId())
        );

        //when
        ConcertDetail actual = getConcertUseCase.getConcertDetail(savedConcert.getId());

        //then
        assertThat(actual.concert()).isEqualTo(savedConcert);
        assertThat(actual.schedules().size()).isNotZero();

    }
}