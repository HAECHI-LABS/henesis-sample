package io.haechi.henesis.assignment.domain.ethklay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public interface FlushedTransactionRepository extends JpaRepository<FlushedTransaction, String> {

    List<FlushedTransaction> findAllByTransactionId(String transactionId);

    Optional<FlushedTransaction> findByTransactionId(String transactionId);

}
