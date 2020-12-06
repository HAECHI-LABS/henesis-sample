package io.haechi.henesis.assignment.domain.transaction;

import io.haechi.henesis.assignment.domain.FlushedTxRepository;
import org.springframework.stereotype.Service;

@Service
public class TxStatusUpdater implements Action{


    private final TransactionRepository transactionRepository;
    private final FlushedTxRepository flushedTxRepository;

    public TxStatusUpdater(TransactionRepository transactionRepository,
                           FlushedTxRepository flushedTxRepository){
        this.transactionRepository = transactionRepository;
        this.flushedTxRepository = flushedTxRepository;
    }

    @Override
    public void doAction(Transaction transaction) {

        // 중복되지 않고 상태가 변한 트랜잭션만 필터링
        if (transactionRepository.findTransactionByDetailIdAndStatus(
                transaction.getDetailId(),
                transaction.getStatus()).isPresent()
            ||flushedTxRepository.findByTxIdAndStatus(
                transaction.getTransactionId(),
                transaction.getStatus()).isPresent()){
            return;
        }

        transactionRepository.updateTxInfo(
                transaction.getStatus(),
                transaction.getUpdatedAt(),
                transaction.getDetailId());

        flushedTxRepository.updateFlushedTxInfo(
                transaction.getStatus(),
                transaction.getId());
    }

}
