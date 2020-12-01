package io.haechi.henesis.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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
    Optional<UserWallet> findAllByWalletId(String walletId);

    @Modifying
    @Query("UPDATE UserWallet u SET u.balance= :walletBalaance, u.updatedAt = :status  WHERE u.walletId = :walletId")
    void updateUserWalletBalanceByWalletId(@Param("walletBalance") String walletBalance, @Param("status") String status, @Param("walletId") String walletId);


}
