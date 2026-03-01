package kr.hhplus.be.server.queue.app;

import kr.hhplus.be.server.PostgresTestcontainersConfig;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.domain.schedule.ScheduleId;
import kr.hhplus.be.server.payment.domain.QueueTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(PostgresTestcontainersConfig.class)
public class QueueServiceTestIt {

    @Autowired
    QueueService queueService;

    @Autowired
    QueueTokenRepository queueTokenRepository;


    @DisplayName("동시성 테스트 - 대기열 토큰 발급")
    @Test
    void 동시성_테스트() throws Exception {
        //given
        UserId userId = new UserId(UUID.randomUUID());
        long scheduleId = 1L;
        int threadCount = 50;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);

        List<IssueQueueTokenResult> results = Collections.synchronizedList(new ArrayList<>());
        for(int i = 0; i < threadCount; i++){
            pool.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    results.add(queueService.issueQueueToken(new IssueQueueTokenCommand(scheduleId, userId)));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    done.countDown();
                }
            });
        }

        // 50개 모두 실행될때까지 대기
        ready.await();
        // 50개 동시 실행 시작
        start.countDown();
        // 50개 모두 종료 대기
        done.await();

        // DB에 1개만 있어야함
        long count = queueTokenRepository.countActiveTokens(ScheduleId.of(scheduleId));
        assertThat(count).isEqualTo(1);

        // 모두 동일한 토큰인지 확인
        long distinctTokenIds = results.stream().map(IssueQueueTokenResult::tokenValue).distinct().count();
        assertThat(distinctTokenIds).isEqualTo(1);

    }
}
