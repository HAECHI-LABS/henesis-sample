package io.haechi.henesis.assignment.domain.transaction;

import io.haechi.henesis.assignment.domain.FlushedTransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusUpdateAction implements UpdateAction {


    private final TransactionRepository transactionRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;

    public StatusUpdateAction(TransactionRepository transactionRepository,
                              FlushedTransactionRepository flushedTransactionRepository){
        this.transactionRepository = transactionRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
    }

    @Override
    public void doAction(Transaction transaction) {

        // 중복되지 않고 상태가 변한 트랜잭션만 필터링
        if (transactionRepository.findTransactionByDetailIdAndStatus(
                transaction.getDetailId(),
                transaction.getStatus()).isPresent()
            || flushedTransactionRepository.findByTxIdAndStatus(
                transaction.getTransactionId(),
                transaction.getStatus()).isPresent()){
            return;
        }

        transactionRepository.updateTxInfo(
                transaction.getStatus(),
                transaction.getUpdatedAt(),
                transaction.getDetailId());

        flushedTransactionRepository.updateFlushedTxInfo(
                transaction.getStatus(),
                transaction.getId());
    }

}
