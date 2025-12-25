package kr.hhplus.be.server.wallet.app;

import kr.hhplus.be.server.PostgresTestcontainersConfig;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.concert.app.domain.FakeIdGenerator;
import kr.hhplus.be.server.reservation.app.IdGenerator;
import kr.hhplus.be.server.wallet.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({PostgresTestcontainersConfig.class})
class GetPointBalanceUseCaseIT {

    @Autowired
    GetPointBalanceUseCase getPointBalanceUseCase;

    @Autowired
    WalletReader walletReader;

    @Autowired
    WalletRepository walletRepository;

    IdGenerator idGenerator = new FakeIdGenerator();

    @Test
    @DisplayName("포인트 조회에 성공한다.")
    void get_point_success() {
        //given
        UserId userId = UserId.of(idGenerator.nextId());
        Wallet wallet = WalletFixture.createWith(userId, Point.of(10000L));
        walletRepository.save(wallet);

        // when
        Point actual = getPointBalanceUseCase.get(userId);

        // then
        walletReader.findPointBalanceByOwnerId(userId);
        assertThat(actual).isEqualTo(wallet.getPoint());

    }
}