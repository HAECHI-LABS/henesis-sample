package io.haechi.henesis.assignment.domain.transaction;


import io.haechi.henesis.assignment.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Wallet> {



    Optional<Transaction> findTransactionByDetailId(int detailsId);


    Optional<Transaction> findTransactionByDetailIdAndStatus(int detailId, String status);

    Transaction findTopByOrderByUpdatedAtDesc();

    Optional<Transaction> findTopBy();

    @Transactional
    @Modifying
    @Query("UPDATE Transaction t SET t.status = :status, t.updatedAt = :updatedAt  WHERE t.detailId = :detailId")
    void updateTxInfo(@Param("status") String status, @Param("updatedAt") String updatedAt, @Param("detailId") int detailId);

}
