package kr.hhplus.be.server.wallet.infra;

import jakarta.persistence.EntityManager;
import kr.hhplus.be.server.common.domain.UserId;
import kr.hhplus.be.server.wallet.domain.Wallet;
import kr.hhplus.be.server.wallet.domain.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WalletRepositoryAdapter implements WalletRepository {

    private final WalletMapper entityMapper;
    private final JpaWalletRepository jpaWalletRepository;
    private final EntityManager em;

    @Override
    public Wallet save(Wallet wallet) {
        JpaWallet saved = jpaWalletRepository.save(JpaWallet.from(wallet));
        return entityMapper.toDomain(saved);
    }

    @Override
    public Optional<Wallet> findByOwnerId(UserId ownerId) {
        return jpaWalletRepository.findByOwnerId(ownerId.value())
                .map(entityMapper::toDomain);
    }

    @Override
    public Wallet saveAndFlush(Wallet wallet) {
        try{
            JpaWallet saved = jpaWalletRepository.saveAndFlush(JpaWallet.from(wallet));
            return entityMapper.toDomain(saved);
        } catch (DataIntegrityViolationException e) {
            em.clear();
            return jpaWalletRepository.findByOwnerId(wallet.getOwnerId().value())
                    .map(entityMapper::toDomain)
                    .orElseThrow();
        }
    }

    @Override
    public Wallet upsertAndReturn(Wallet wallet) {
        jpaWalletRepository.upsertAndReturn(wallet.getId().value(), wallet.getOwnerId().value());
        return wallet;
    }

}
