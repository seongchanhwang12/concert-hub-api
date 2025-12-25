package kr.hhplus.be.server.wallet.app;

import kr.hhplus.be.server.common.domain.Money;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.app.dto.ChargePointCommand;
import kr.hhplus.be.server.wallet.app.dto.ChargePointResult;
import kr.hhplus.be.server.wallet.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChargePointUseCase {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletTransactionFactory walletTransactionFactory;
    private final WalletFactory walletFactory;

    @Transactional
    public ChargePointResult chargePoint(ChargePointCommand cmd) {
        final Money amount = Money.wons(cmd.amount());
        final UserId userId = cmd.userId();
        final UUID idempotencyKey = cmd.IdempotencyKey();

        Wallet wallet = walletRepository.findByOwnerId(userId)
                .orElseGet(()-> walletFactory.empty(userId));

        wallet.charge(Point.of(amount.value()));
        WalletTransaction chargeTx = walletTransactionFactory.createCharge(wallet, idempotencyKey);

        // 이미 충전된 내역인 경우 리턴 (성공)
        if(walletTransactionRepository.trySaveIdempotency(chargeTx)){
            // 최초 진행시 save
            walletRepository.save(wallet);
            return ChargePointResult.from(chargeTx);
        };

        WalletTransaction foundChargeTx = walletTransactionRepository.findByOwnerIdAndIdempotencyKey(userId, idempotencyKey)
                .orElseThrow();

        // unique 제약 충돌일 경우
        return ChargePointResult.from(foundChargeTx);

    }
}
