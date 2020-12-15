package io.haechi.henesis.assignment.domain;


import io.haechi.henesis.assignment.domain.ethklay.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Wallet> {


    Optional<Transaction> findByDetailId(int detailId);

    List<Transaction> findAllByDetailId(int detailId);

    Optional<Transaction> findTopByOrderByUpdatedAtDesc();
}
