package io.haechi.henesis.assignment.application;

import io.haechi.henesis.assignment.domain.EthKlayHenesisWalletClient;
import io.haechi.henesis.assignment.domain.FlushedTransactionRepository;
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

    private final EthKlayHenesisWalletClient ethKlayHenesisWalletClient;
    private final UserWalletRepository userWalletRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final ActionSupplier<UpdateAction> updateActionSupplier;

    private String updatedAt = Long.toString(System.currentTimeMillis());

    public MonitoringApplicationService(EthKlayHenesisWalletClient ethKlayHenesisWalletClient,
                                        UserWalletRepository userWalletRepository,
                                        FlushedTransactionRepository flushedTransactionRepository,
                                        TransactionRepository transactionRepository,
                                        ActionSupplier<UpdateAction> updateActionSupplier) {
        this.ethKlayHenesisWalletClient = ethKlayHenesisWalletClient;
        this.userWalletRepository = userWalletRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
        this.transactionRepository = transactionRepository;
        this.updateActionSupplier = updateActionSupplier;
    }


    @Scheduled(fixedRate = 2000, initialDelay = 2000)
    public void getValueTransferEvents(){

        // Call Wallet API (Value Transfer Events)
        // Not flushed Tx
        List<Transaction> transactions = ethKlayHenesisWalletClient.getValueTransferEvents(updatedAt).stream()
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
                .filter(tx -> flushedTransactionRepository.findByTxId(tx.getTransactionId()).isEmpty())
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
        List<Wallet> wallets = ethKlayHenesisWalletClient.getAllUserWallet().stream()
                .filter(u->!userWalletRepository.existsUserWalletByWalletIdAndStatus(u.getWalletId(), u.getStatus()))
                .collect(Collectors.toList());
        wallets.forEach(u->
                userWalletRepository.updateWalletInfo(u.getStatus(),u.getUpdatedAt(),u.getWalletId())
        );
    }



    public void mappingActionBy(Transaction transaction ,Situation situation){
        // 상황에 따른 액션 맵핑
        updateActionSupplier.supply(situation).doAction(transaction);
    }
}
