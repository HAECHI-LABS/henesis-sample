package io.haechi.henesis.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface DepositAddressRepository extends JpaRepository<DepositAddress, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DepositAddress> findByAddress(String address);

    List<DepositAddress> findAllByBlockchainAndStatus(Blockchain blockchain, DepositAddress.Status status);
}
