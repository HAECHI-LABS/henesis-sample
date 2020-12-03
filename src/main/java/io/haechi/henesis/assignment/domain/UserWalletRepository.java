package io.haechi.henesis.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;



@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, String>, JpaSpecificationExecutor<UserWallet> {

    /*
    @Transactional
    @Query("SELECT u from UserWallet u WHERE u.walletId = :walletId")
    Optional<UserWallet> getUserWalletsByWalletId(@Param("walletId")final String walletId);
    @Transactional
    UserWallet findUserWalletByWalletIdIsNotNull(final String walletId);
    */

    Optional<UserWallet> findByWalletId(String walletId);
    boolean existsUserWalletByWalletIdAndStatus(String walletId, String status);

    @Transactional
    @Modifying
    @Query("UPDATE UserWallet u SET u.balance = :balance WHERE u.walletId = :walletId")
    void updateWalletBalance(@Param("balance") Amount balance, @Param("walletId") String walletId);

    @Transactional
    @Modifying
    @Query("UPDATE UserWallet u SET u.status = :status, u.updatedAt = :updatedAt  WHERE u.walletId = :walletId")
    void updateWalletInfo(@Param("status") String status, @Param("updatedAt") String updatedAt, @Param("walletId") String walletId);


}
