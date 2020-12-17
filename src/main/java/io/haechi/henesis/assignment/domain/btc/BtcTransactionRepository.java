package io.haechi.henesis.assignment.domain.btc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BtcTransactionRepository extends JpaRepository<BtcTransaction, String>, JpaSpecificationExecutor<BtcTransaction> {

    List<BtcTransaction> findAllByTransactionId(String transactionId);

    Optional<BtcTransaction> findTopByOrderByUpdatedAtDesc();

    Optional<BtcTransaction> findTopByOrderByUpdatedAtAsc();

    boolean existsByTransactionIdAndStatus(
            @Param("transactionId") String transactionId,
            @Param("status") String status
    );
}
