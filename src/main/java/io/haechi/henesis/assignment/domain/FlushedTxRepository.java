package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.FlushedTx;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlushedTxRepository extends JpaRepository<FlushedTx, String> {
}
