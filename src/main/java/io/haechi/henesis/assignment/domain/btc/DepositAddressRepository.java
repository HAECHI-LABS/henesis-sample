package io.haechi.henesis.assignment.domain.btc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface DepositAddressRepository extends JpaRepository<DepositAddress, String>, JpaSpecificationExecutor<DepositAddress> {
    Optional<DepositAddress> findByDepositAddressId(String depositAddressId);
}
