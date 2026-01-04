package kr.hhplus.be.server.wallet.app;

import kr.hhplus.be.server.PostgresTestcontainersConfig;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.reservation.app.IdGenerator;
import kr.hhplus.be.server.wallet.app.dto.ChargePointCommand;
import kr.hhplus.be.server.wallet.app.dto.ChargePointCommandFixture;
import kr.hhplus.be.server.wallet.app.dto.ChargePointResult;
import kr.hhplus.be.server.wallet.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@Slf4j
@Import({PostgresTestcontainersConfig.class})
class ChargePointUseCaseIT {

    @Autowired
    ChargePointUseCase chargePointUseCase;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    WalletTransactionRepository walletTransactionRepository;

    IdGenerator idGenerator = new FakeIdGenerator();


    // @BeforeEach
    void setUp(){
        WalletTransactionFactory walletTransactionFactory = new WalletTransactionFactory(new FakeIdGenerator());
        WalletFactory walletFactory = new WalletFactory(new FakeIdGenerator());
        chargePointUseCase = new ChargePointUseCase(walletRepository, walletTransactionRepository, walletTransactionFactory,walletFactory);
    }

    @Test
    @DisplayName("이미 멱등성키로 등록된 요청이 있는 경우_성공")
    void when_exist_transaction_success() {
        //given
        Point chargeAmount = Point.of(1000L);
        Point balanceAfter = Point.of(2000L);
        UserId userId = UserId.of(UUID.randomUUID());
        UUID idempotencyKey = UUID.randomUUID();

        Wallet wallet = WalletFixture.createWith(userId, Point.of(1000L));
        WalletTransaction chargeTx = WalletTransactionFixture.createCharge(wallet, chargeAmount, idempotencyKey);
        walletTransactionRepository.save(chargeTx);

        ChargePointCommand cmd = ChargePointCommandFixture.createWith(idempotencyKey, userId, chargeAmount);

        //when
        ChargePointResult chargePointResult = chargePointUseCase.chargePoint(cmd);

        //then
        assertThat(chargePointResult.chargedPoint()).isEqualTo(chargeAmount);
        assertThat(chargePointResult.balanceAfter()).isEqualTo(balanceAfter);
    }

    @Test
    @DisplayName("등록된 요청이 없고, wallet 도 없는 경우_성공")
    void when_not_exist_transaction_success() {
        //given
        Point chargeAmount = Point.of(1000L);
        UserId userId = UserId.of(UUID.randomUUID());
        UUID idempotencyKey = UUID.randomUUID();

        ChargePointCommand cmd = ChargePointCommandFixture.createWith(idempotencyKey, userId, chargeAmount);

        //when
        ChargePointResult chargePointResult = chargePointUseCase.chargePoint(cmd);

        //then
        assertThat(chargePointResult.chargedPoint()).isEqualTo(chargeAmount);
        assertThat(chargePointResult.balanceAfter()).isEqualTo(chargeAmount);
    }

    /**
     * 검증 :
     *  - 먼저 접근한 스레드가 지갑을 생성하고, 나중에 접근한 스레드가 지갑 저장시 Unique 제약 조건 예외가 발생한다.
     *  - unique 예외 발생시 지갑을 조회해 저장한다.
     *  - 생성한 지갑 정보로 지갑 거래 내역을 생성한다.
     *  -
     * @throws InterruptedException
     */
    @Test
    @DisplayName("동시성 테스트 : 2개의스레드 동시 접근시, 등록된 멱등키가 없고, 지갑이 저장되지 않은 상황")
    void concurrency_test() throws InterruptedException {
        //given
        Point chargeAmount = Point.of(1000L);
        UserId userId = UserId.of(UUID.randomUUID());
        UUID idempotencyKey = idGenerator.nextId();

        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        ChargePointCommand cmd = ChargePointCommandFixture.createWith(idempotencyKey, userId, chargeAmount);

        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    chargePointUseCase.chargePoint(cmd);
                    successCount.incrementAndGet();
                } catch (InterruptedException e) {
                    failCount.incrementAndGet();
                    log.error(e.getMessage(), e);
                } catch (Exception e) {
                    log.error("예상치 못한 에러 {}",e.getMessage(), e);
                } finally {
                    finishLatch.countDown();
                }

            });
        }

        startLatch.countDown();
        finishLatch.await();

        //then
        assertThat(successCount.get()).isEqualTo(threadCount);
        assertThat(failCount.get()).isEqualTo(0);
    }


}