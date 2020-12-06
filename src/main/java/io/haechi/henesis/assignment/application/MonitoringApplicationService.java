package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.domain.Exchange;
import io.haechi.henesis.assignment.domain.FlushedTxRepository;
import io.haechi.henesis.assignment.domain.UserWalletRepository;
import io.haechi.henesis.assignment.domain.Wallet;
import io.haechi.henesis.assignment.domain.transaction.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MonitoringApplicationService {

    private final Exchange exchange;
    private final UserWalletRepository userWalletRepository;
    private final FlushedTxRepository flushedTxRepository;
    private final TransactionRepository transactionRepository;
    private final ActionSupplier<Action> actionActionSupplier;

    private String updatedAt = Long.toString(System.currentTimeMillis());

    public MonitoringApplicationService(Exchange exchange,
                                        UserWalletRepository userWalletRepository,
                                        FlushedTxRepository flushedTxRepository,
                                        TransactionRepository transactionRepository,
                                        ActionSupplier<Action> actionSupplier) {
        this.exchange = exchange;
        this.userWalletRepository = userWalletRepository;
        this.flushedTxRepository = flushedTxRepository;
        this.transactionRepository = transactionRepository;
        this.actionActionSupplier = actionSupplier;
    }


    @Scheduled(fixedRate = 2000, initialDelay = 2000)
    public void getValueTransferEvents(){

        // Call Wallet API (Value Transfer Events)
        // Not flushed Tx
        List<Transaction> transactions = exchange.getValueTransferEvents(updatedAt).stream()
                .filter(t -> t.getStatus().contains("CONFIRMED")
                        || t.getStatus().contains("REVERTED")
                        || t.getStatus().contains("FAILED"))
                .collect(Collectors.toList());


        // 잔고 업데이트, 상태 업데이트 상황 별 액션 서플라이어 입니다.
        // 트랜잭션 상태, 타입별 Situation <-> Action Mapping 후 Update Balance
        transactions.forEach(tx-> mappingActionBy(tx,tx.situation()));



        // Save only New and Not Flushed Transactions
        List<Transaction> newTx = transactions.stream()
                .filter(tx -> transactionRepository.findTransactionByDetailId(tx.getDetailId()).isEmpty())
                .filter(tx -> flushedTxRepository.findByTxId(tx.getTransactionId()).isEmpty())
                .collect(Collectors.toList());

        transactionRepository.saveAll(newTx);


        if (transactionRepository.findTopBy().isPresent())
            updatedAt = transactionRepository.findTopByOrderByUpdatedAtDesc().getUpdatedAt();
    }


    //지갑 상태 업데이트 (CREATING -> ACTIVE or INACTIVE)
    @Async
    @Scheduled(fixedRate =1000, initialDelay = 2000)
    public void getUserWalletInfo(){

        // 모든 사용자 지갑
        List<Wallet> wallets = exchange.getAllUserWallet().stream()
                .filter(u->!userWalletRepository.existsUserWalletByWalletIdAndStatus(u.getWalletId(), u.getStatus()))
                .collect(Collectors.toList());
        wallets.forEach(u->
                userWalletRepository.updateWalletInfo(u.getStatus(),u.getUpdatedAt(),u.getWalletId())
        );
    }



    public void mappingActionBy(Transaction transaction ,Situation situation){
        // 상황에 따른 액션 맵핑
        actionActionSupplier.supply(situation).doAction(transaction);
    }
}
