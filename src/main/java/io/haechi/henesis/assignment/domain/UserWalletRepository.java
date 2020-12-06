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
public interface UserWalletRepository extends JpaRepository<Wallet, String>, JpaSpecificationExecutor<Wallet> {


    Optional<Wallet> findByWalletId(String walletId);

    boolean existsUserWalletByWalletIdAndStatus(String walletId, String status);

    @Transactional
    @Modifying
    @Query("UPDATE Wallet u SET u.balance = :balance WHERE u.walletId = :walletId")
    void updateWalletBalance(@Param("balance") Amount balance, @Param("walletId") String walletId);

    @Transactional
    @Modifying
    @Query("UPDATE Wallet u SET u.status = :status, u.updatedAt = :updatedAt  WHERE u.walletId = :walletId")
    void updateWalletInfo(@Param("status") String status, @Param("updatedAt") String updatedAt, @Param("walletId") String walletId);


}
