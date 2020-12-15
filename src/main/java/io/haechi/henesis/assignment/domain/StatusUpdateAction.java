package io.haechi.henesis.assignment.domain;

import org.springframework.stereotype.Service;


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
