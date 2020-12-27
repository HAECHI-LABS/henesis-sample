package io.haechi.henesis.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByDepositAddressAndSymbol(DepositAddress depositAddress, String symbol);
}
