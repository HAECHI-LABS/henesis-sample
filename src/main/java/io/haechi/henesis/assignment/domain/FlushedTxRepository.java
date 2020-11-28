package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.FlushedTx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
@Repository
public interface FlushedTxRepository extends JpaRepository<FlushedTx, String> {

    @Transactional
    @Query("SELECT f FROM FlushedTx f WHERE f.txId = :txId")
    Optional<FlushedTx> getFlushedTxByTxId(@Param("txId") final String txId);

    @Transactional
    @Query("SELECT f FROM FlushedTx f WHERE f.txId = :txId")
    FlushedTx findFlushedTxByTxId(@Param("txId") final String txId);
}

