package io.haechi.henesis.assignment.ethKlayDomain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FlushedTransactionRepository extends JpaRepository<FlushedTransaction, String> {

    List<FlushedTransaction> findAllByTransactionId(String transactionId);
    Optional<FlushedTransaction> findByTransactionId(String transactionId);

    boolean existsByTransactionIdAndStatus(
            @Param("transactionId") String transactionId,
            @Param("status") String status
    );

}
