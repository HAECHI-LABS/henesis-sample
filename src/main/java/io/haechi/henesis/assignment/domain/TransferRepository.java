package io.haechi.henesis.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByHenesisId(String henesisId);

    Optional<Transfer> findTopByBlockchainOrderByHenesisUpdatedAt(Blockchain blockchain);

    @Query(
            value = "SELECT SUM(t.amount.value) " +
                    "FROM Transfer t " +
                    "WHERE t.depositAddressId = :depositAddressId " +
                    "AND t.blockchain = :blockchain " +
                    "AND t.symbol = :symbol " +
                    "AND t.status IN :statuses"
    )
    Optional<BigInteger> sumUnconfirmedAmount(
            @Param("depositAddressId") Long depositAddressId,
            @Param("blockchain") Blockchain blockchain,
            @Param("symbol") String symbol,
            @Param("statuses") List<Transfer.Status> statuses
    );
}
