package kr.hhplus.be.server.wallet.infra;

import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.domain.WalletTransaction;
import kr.hhplus.be.server.wallet.domain.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WalletTransactionRepositoryAdapter implements WalletTransactionRepository {

    private final JpaWalletTransactionRepository japWalletRepository;
    private final WalletTransactionEntityMapper entityMapper;

    @Override
    public void save(WalletTransaction walletTransaction) {
        japWalletRepository.save(entityMapper.toEntity(walletTransaction));
    }

    @Override
    public boolean trySaveIdempotency(WalletTransaction walletTx) {
        return  japWalletRepository.saveIdempotency(
                walletTx.getId().value(),
                walletTx.getWalletId().value(),
                walletTx.getOwnerId().value(),
                walletTx.getType().name(),
                walletTx.getPointAmount().balance(),
                walletTx.getBalanceAfter().balance(),
                walletTx.getIdempotencyKey()) == 1;
    }


    @Override
    public Optional<WalletTransaction> findByOwnerIdAndIdempotencyKey(UserId userId, UUID idempotencyKey) {
        return Optional.ofNullable(japWalletRepository.findByUserIdAndIdempotencyKey(userId.value(), idempotencyKey))
                .map(entityMapper::toDomain);
    }
}
