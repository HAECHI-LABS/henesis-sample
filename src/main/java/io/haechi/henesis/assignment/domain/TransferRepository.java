package io.haechi.henesis.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByHenesisId(String henesisId);

    Optional<Transfer> findTopByBlockchainOrderByHenesisUpdatedAt(Blockchain blockchain);
}
