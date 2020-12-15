package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class StatusUpdateAction implements UpdateAction {

    private final TransactionRepository transactionRepository;

    public StatusUpdateAction(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void doAction(Transaction transaction) {
        transactionRepository.findByDetailId(
                transaction.getDetailId()
        ).ifPresent(
                tx -> {
                    tx.setStatus(transaction.getStatus());
                    tx.setUpdatedAt(transaction.getUpdatedAt());
                    transactionRepository.save(tx);
                }
        );

    }
}
