package io.haechi.henesis.assignment.domain.repository;

import io.haechi.henesis.assignment.domain.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserWalletRepository extends JpaRepository<UserWallet, String> {

    @Query("SELECT wallet from UserWallet wallet WHERE wallet.walletId = :walletId")
    UserWallet getUserWalletByWalletId(@Param("walletId") final String walletId);

}
