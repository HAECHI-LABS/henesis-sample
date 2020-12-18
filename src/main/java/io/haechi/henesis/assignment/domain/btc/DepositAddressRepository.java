package io.haechi.henesis.assignment.domain.btc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface DepositAddressRepository extends JpaRepository<DepositAddress, String>, JpaSpecificationExecutor<DepositAddress> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DepositAddress> findByAddress(String address);
}
