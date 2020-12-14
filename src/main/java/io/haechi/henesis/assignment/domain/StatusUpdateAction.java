package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import org.springframework.stereotype.Service;


@Service
public class StatusUpdateAction implements UpdateAction {


    private final TransactionRepository transactionRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;

    public StatusUpdateAction(TransactionRepository transactionRepository,
                              FlushedTransactionRepository flushedTransactionRepository) {
        this.transactionRepository = transactionRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
    }

    @Override
    public void doAction(Transaction transaction) {

        transactionRepository.findByDetailId(
                transaction.getDetailId()
        ).ifPresent(
                t -> {
                    t.setStatus(transaction.getStatus());
                    t.setUpdatedAt(transaction.getUpdatedAt());
                    transactionRepository.save(t);
                });


        flushedTransactionRepository.findByTransactionId(
                transaction.getTransactionId()
        ).ifPresent(f -> {
            f.setStatus(transaction.getStatus());
            f.setUpdatedAt(transaction.getUpdatedAt());
            flushedTransactionRepository.save(f);
        });
    }

}
