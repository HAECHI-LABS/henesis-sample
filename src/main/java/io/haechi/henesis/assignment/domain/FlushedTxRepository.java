package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.FlushedTx;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlushedTxRepository extends JpaRepository<FlushedTx, String> {
    Optional<FlushedTx> findByTxId(String txId);
    Optional<FlushedTx> findAllByTxId(String txId);
    boolean existsAllByTxId(String txId);
}
