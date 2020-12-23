package io.haechi.henesis.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllByHenesisId(String henesisId);

    Optional<Transfer> findTopByOrderByUpdatedAtDesc();
}
