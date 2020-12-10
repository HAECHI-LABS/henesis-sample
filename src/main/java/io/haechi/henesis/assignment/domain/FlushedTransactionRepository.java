package io.haechi.henesis.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface FlushedTransactionRepository extends JpaRepository<FlushedTransaction, String> {

    Optional<FlushedTransaction> findByTxId(String txId);
    Optional<FlushedTransaction> findByTxIdAndStatus(String txId, String status);
    @Transactional
    @Modifying
    @Query("UPDATE FlushedTransaction f SET f.status = :status WHERE f.id = :id")
    void updateFlushedTxInfo(@Param("status") String status, @Param("id") int id);

}
