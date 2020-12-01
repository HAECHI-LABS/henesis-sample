package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.application.dto.TransactionDTO;
import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.transaction.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoringApplicationService {

    private final WalletService walletService;
    private final UserWalletRepository userWalletRepository;
    private final FlushedTxRepository flushedTxRepository;
    private final TransactionRepository transactionRepository;
    private final ActionSupplier<Action> actionActionSupplier;

    public MonitoringApplicationService(WalletService walletService,
                                        UserWalletRepository userWalletRepository,
                                        FlushedTxRepository flushedTxRepository,
                                        TransactionRepository transactionRepository,
                                        ActionSupplier<Action> actionActionSupplier) {
        this.walletService = walletService;
        this.userWalletRepository = userWalletRepository;
        this.flushedTxRepository = flushedTxRepository;
        this.transactionRepository = transactionRepository;
        this.actionActionSupplier = actionActionSupplier;
    }


    public void getValueTransferEvents(){

        // API 호출
        List<Transaction> transactions = walletService.getValueTransferEvents();

        // 트랜잭션 상태, 타입별 Situation <-> Action Mapping 후 Update Balance
        transactions.forEach(tx->
                        updateBalanceBy(
                                tx,
                                Transaction.of(
                                        tx.getDetailId(),
                                        tx.getFrom(),
                                        tx.getTo(),
                                        tx.getAmount(),
                                        tx.getBlockchain(),
                                        tx.getStatus(),
                                        tx.getTransferType(),
                                        tx.getCoinSymbol(),
                                        tx.getConfirmation(),
                                        tx.getTransactionId(),
                                        tx.getTransactionHash(),
                                        tx.getCreatedAt(),
                                        tx.getUpdatedAt(),
                                        tx.getWalletId(),
                                        tx.getWalletName()
                                ).situation()
                        )
        );
        transactionRepository.saveAll(transactions);
    }

    public void updateBalanceBy(Transaction transaction ,Situation situation){
        // 상황에 따른 액션 맵핑
        actionActionSupplier.supply(situation).updateBalanceBy(transaction);

    }
    /*
                                    Transaction.of(
                                    tx.getDetailId(),
                                    tx.getFrom(),
                                    tx.getTo(),
                                    tx.getAmount(),
                                    tx.getBlockchain(),
                                    tx.getStatus(),
                                    tx.getTransferType(),
                                    tx.getCoinSymbol(),
                                    tx.getConfirmation(),
                                    tx.getTransactionId(),
                                    tx.getTransactionHash(),
                                    tx.getCreatedAt(),
                                    tx.getUpdatedAt(),
                                    tx.getWalletId(),
                                    tx.getWalletName()
                                ).situation()
     */
}
