package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.transaction.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitoringApplicationService {

    private final Exchange exchange;
    private final UserWalletRepository userWalletRepository;
    private final FlushedTxRepository flushedTxRepository;
    private final TransactionRepository transactionRepository;
    private final ActionSupplier<Action> actionActionSupplier;

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


    @Scheduled(fixedRate = 1000, initialDelay = 2000)
    public void getValueTransferEvents(){

        String updatedAt = transactionRepository.findTopByOrderByUpdatedAtDesc().getUpdatedAt();

        // Value Transfer Events API 를 호출합니다.
        List<Transaction> transactions = exchange.getValueTransferEvents(updatedAt).stream()
                .filter(t -> t.getStatus().contains("CONFIRMED")
                        || t.getStatus().contains("REVERTED")
                        || t.getStatus().contains("FAILED"))
                .collect(Collectors.toList());

        // 잔고 업데이트, 상태 업데이트 상황 별 액션 서플라이어 입니다.
        // 트랜잭션 상태, 타입별 Situation <-> Action Mapping 후 Update Balance
        transactions.forEach(tx-> mappingActionBy(tx,tx.situation()));


        // 집금되지 않은 새로운 트랜잭션만 저장합니다.
        List<Transaction> newTx = transactions.stream()
                .filter(tx -> !transactionRepository.existsTransactionByDetailId(tx.getDetailId()))
                .filter(tx -> !flushedTxRepository.existsAllByTxId(tx.getTransactionId()))
                .collect(Collectors.toList());

        newTx.forEach(tx->System.out.println("new : "+tx.getTransactionId()+" "+tx.getWalletName()+" "+tx.getTransferType()+" "+tx.getStatus()));

        transactionRepository.saveAll(newTx);
    }


    //지갑 상태 업데이트 (CREATING -> ACTIVE or INACTIVE)
    @Async
    @Scheduled(fixedRate =1000, initialDelay = 2000)
    public void getUserWalletInfo(){

        // 모든 사용자 지갑
        List<UserWallet> userWallets = exchange.getAllUserWallet().stream()
                .filter(u->!userWalletRepository.existsUserWalletByWalletIdAndStatus(u.getWalletId(), u.getStatus()))
                .collect(Collectors.toList());
        userWallets.forEach(u->
                userWalletRepository.updateWalletInfo(u.getStatus(),u.getUpdatedAt(),u.getWalletId())
        );
    }




    public void mappingActionBy(Transaction transaction ,Situation situation){
        // 상황에 따른 액션 맵핑
        actionActionSupplier.supply(situation).doAction(transaction);
    }
}
