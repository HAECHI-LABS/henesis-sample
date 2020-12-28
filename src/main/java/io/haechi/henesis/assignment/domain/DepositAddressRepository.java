package io.haechi.henesis.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface DepositAddressRepository extends JpaRepository<DepositAddress, Long> {
    Optional<DepositAddress> findByAddress(String address);

    List<DepositAddress> findAllByBlockchainAndStatus(Blockchain blockchain, DepositAddress.Status status);

    boolean existsByHenesisId(String henesisId);

    List<DepositAddress> findAllByBlockchain(Blockchain blockchain);

    List<DepositAddress> findAllByBlockchainAndIdIn(Blockchain blockchain, List<Long> ids);
}
