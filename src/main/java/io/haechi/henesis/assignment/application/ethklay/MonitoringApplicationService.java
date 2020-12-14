package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.ethklay.EthKlayHenesisWalletClient;
import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import io.haechi.henesis.assignment.domain.ethklay.Wallet;
import io.haechi.henesis.assignment.domain.ethklay.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MonitoringApplicationService {

    private final EthKlayHenesisWalletClient ethHenesisWalletClient;
    private final EthKlayHenesisWalletClient klayHenesisWalletClient;
    private final WalletRepository walletRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final ActionSupplier<UpdateAction> updateActionSupplier;

    private String updatedAt = Long.toString(System.currentTimeMillis());

    public MonitoringApplicationService(@Qualifier("ethHenesisWalletService") EthKlayHenesisWalletClient ethHenesisWalletClient,
                                        @Qualifier("klayHenesisWalletService") EthKlayHenesisWalletClient klayHenesisWalletClient,
                                        WalletRepository walletRepository,
                                        FlushedTransactionRepository flushedTransactionRepository,
                                        TransactionRepository transactionRepository,
                                        ActionSupplier<UpdateAction> updateActionSupplier) {
        this.ethHenesisWalletClient = ethHenesisWalletClient;
        this.klayHenesisWalletClient = klayHenesisWalletClient;
        this.walletRepository = walletRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
        this.transactionRepository = transactionRepository;
        this.updateActionSupplier = updateActionSupplier;
    }


    @Scheduled(fixedRate = 2000, initialDelay = 2000)
    public void getEthValueTransferEvents() {

        // Call Wallet API (Value Transfer Events)
        // Not flushed Tx
        List<Transaction> ethTransactions = ethHenesisWalletClient.getValueTransferEvents(updatedAt).stream()
                .filter(t -> t.getStatus().contains("CONFIRMED")
                        || t.getStatus().contains("REVERTED")
                        || t.getStatus().contains("FAILED"))
                .collect(Collectors.toList());

        List<Transaction> klayTransactions = klayHenesisWalletClient.getValueTransferEvents(updatedAt).stream()
                .filter(t -> t.getStatus().contains("CONFIRMED")
                        || t.getStatus().contains("REVERTED")
                        || t.getStatus().contains("FAILED"))
                .collect(Collectors.toList());
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(ethTransactions);
        transactions.addAll(klayTransactions);

        // 잔고 업데이트, 상태 업데이트 상황 별 액션 서플라이어 입니다.
        // 트랜잭션 상태, 타입별 Situation <-> Action Mapping 후 Update Balance
        transactions.forEach(tx -> mappingActionBy(tx, tx.situation()));


        // Save only New and Not Flushed Transactions
        List<Transaction> newTransaction = transactions.stream()
                .filter(tx -> transactionRepository.findAllByDetailIdAndStatus(tx.getDetailId(), tx.getStatus()).isEmpty())
                .filter(tx -> flushedTransactionRepository.findAllByTransactionId(tx.getTransactionId()).isEmpty())
                .collect(Collectors.toList());

        transactionRepository.saveAll(newTransaction);

        transactionRepository.findTopByOrderByUpdatedAtDesc().ifPresent(u -> {
            this.updatedAt = u.getUpdatedAt();
        });

    }


    //지갑 상태 업데이트 (CREATING -> ACTIVE or INACTIVE)
    @Async
    @Scheduled(fixedRate = 1000, initialDelay = 2000)
    public void getUserWalletInfo() {


        // 모든 사용자 지갑
        List<Wallet> ethWallets = ethHenesisWalletClient.getAllUserWallet().stream()
                .filter(u -> !walletRepository.existsUserWalletByWalletIdAndStatus(u.getWalletId(), u.getStatus()))
                .collect(Collectors.toList());
        List<Wallet> klayWallets = klayHenesisWalletClient.getAllUserWallet().stream()
                .filter(u -> !walletRepository.existsUserWalletByWalletIdAndStatus(u.getWalletId(), u.getStatus()))
                .collect(Collectors.toList());

        List<Wallet> wallets = new ArrayList<>();
        wallets.addAll(ethWallets);
        wallets.addAll(klayWallets);

        wallets.forEach(u ->
                walletRepository.updateWalletInfo(u.getStatus(), u.getUpdatedAt(), u.getWalletId())
        );
    }


    public void mappingActionBy(Transaction transaction, Situation situation) {
        // 상황에 따른 액션 맵핑
        updateActionSupplier.supply(situation).doAction(transaction);
    }
}
