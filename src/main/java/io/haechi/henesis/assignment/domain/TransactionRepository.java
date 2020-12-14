package io.haechi.henesis.assignment.domain;


import io.haechi.henesis.assignment.domain.ethklay.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Wallet> {


    Optional<Transaction> findByDetailId(int detailId);

    List<Transaction> findAllByDetailIdAndStatus(int detailId, String status);

    Optional<Transaction> findTopByOrderByUpdatedAtDesc();

    boolean existsByTransactionIdAndStatus(
            @Param("transactionId") String transactionId,
            @Param("status") String status
    );

    boolean existsByTransactionIdAndTransferType(
            @Param("transactionId") String transactionId,
            @Param("transferType") String transferType
    );

}
