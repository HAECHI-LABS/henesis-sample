package io.haechi.henesis.assignment.domain.ethklay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface WalletRepository extends JpaRepository<Wallet, String>, JpaSpecificationExecutor<Wallet> {


    Optional<Wallet> findByWalletId(String walletId);

    boolean existsUserWalletByWalletIdAndStatus(String walletId, String status);


}
