package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.ethklay.Transaction;
import io.haechi.henesis.assignment.domain.ethklay.TransactionRepository;
import io.haechi.henesis.assignment.domain.UpdateAction;
import io.haechi.henesis.assignment.domain.ethklay.EthKlayWalletService;
import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import io.haechi.henesis.assignment.domain.ethklay.Wallet;
import io.haechi.henesis.assignment.domain.ethklay.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MonitoringApplicationService {

    private final EthKlayWalletService ethHenesisWalletService;
    private final EthKlayWalletService klayHenesisWalletService;
    private final WalletRepository walletRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final ActionSupplier<UpdateAction> updateActionSupplier;

    private String updatedAt = Long.toString(System.currentTimeMillis());

    public MonitoringApplicationService(
            @Qualifier("ethHenesisWalletService") EthKlayWalletService ethHenesisWalletService,
            @Qualifier("klayHenesisWalletService") EthKlayWalletService klayHenesisWalletService,
            WalletRepository walletRepository,
            FlushedTransactionRepository flushedTransactionRepository,
            TransactionRepository transactionRepository,
            ActionSupplier<UpdateAction> updateActionSupplier
    ) {
        this.ethHenesisWalletService = ethHenesisWalletService;
        this.klayHenesisWalletService = klayHenesisWalletService;
        this.walletRepository = walletRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
        this.transactionRepository = transactionRepository;
        this.updateActionSupplier = updateActionSupplier;
    }

    @Transactional
    @Scheduled(fixedRate = 2000, initialDelay = 500)
    public void updateTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(
                ethHenesisWalletService.getTransactions(updatedAt).stream()
                        .filter(Transaction::isDesired)
                        .collect(Collectors.toList())
        );
        transactions.addAll(
                klayHenesisWalletService.getTransactions(updatedAt).stream()
                        .filter(Transaction::isDesired)
                        .collect(Collectors.toList())
        );

        List<Transaction> newTransaction = transactions.stream()
                .filter(tx -> transactionRepository.findAllByTransactionId(tx.getTransactionId()).isEmpty())
                .filter(tx -> flushedTransactionRepository.findAllByTransactionId(tx.getTransactionId()).isEmpty())
                .collect(Collectors.toList());
        transactionRepository.saveAll(newTransaction);
        newTransaction.forEach(tx -> updateActionSupplier.supply(tx.situation()).updateBalance(tx));

        transactionRepository.findTopByOrderByUpdatedAtDesc().ifPresent(u -> {
            this.updatedAt = u.getUpdatedAt();
        });

    }


    //지갑 상태 업데이트 (CREATING, ACTIVE, INACTIVE)
    @Async
    @Transactional
    @Scheduled(fixedRate = 6000, initialDelay = 2000)
    public void updateWalletStatus() {
        // 모든 사용자 지갑
        List<Wallet> wallets = new ArrayList<>();
        wallets.addAll(ethHenesisWalletService.getAllUserWallet().stream()
                .filter(e -> walletRepository.findByWalletId(e.getWalletId()).isEmpty()).collect(Collectors.toList())
        );
        wallets.addAll(klayHenesisWalletService.getAllUserWallet().stream()
                .filter(k -> walletRepository.findByWalletId(k.getWalletId()).isEmpty())
                .collect(Collectors.toList())
        );
        wallets.forEach(wallet ->
                walletRepository.findByWalletId(wallet.getWalletId())
                        .ifPresent(e -> {
                            e.setStatus(wallet.getStatus());
                            e.setUpdatedAt(wallet.getUpdatedAt());
                            walletRepository.save(e);
                        })
        );
    }

    @Async
    @Transactional
    @Scheduled(fixedRate = 5000, initialDelay = 1000)
    public void updateFlushedTransactionStatus() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(
                ethHenesisWalletService.getTransactions(updatedAt).stream()
                        .filter(Transaction::isDesired)
                        .collect(Collectors.toList())
        );
        transactions.addAll(
                klayHenesisWalletService.getTransactions(updatedAt).stream()
                        .filter(Transaction::isDesired)
                        .collect(Collectors.toList())
        );

        transactions.forEach(tx -> {
            flushedTransactionRepository.findByTransactionId(tx.getTransactionId())
                    .ifPresent(f -> {
                        f.setStatus(tx.getStatus());
                        f.setUpdatedAt(tx.getUpdatedAt());
                        flushedTransactionRepository.save(f);
                    });
        });
    }
}
