package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.UpdateAction;
import io.haechi.henesis.assignment.domain.ethklay.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class MonitoringApplicationService {

    private final EthKlayWalletService ethKlayHenesisWalletService;
    private final WalletRepository walletRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final ActionSupplier<UpdateAction> updateActionSupplier;

    private String updatedAtGte = Long.toString(System.currentTimeMillis());

    public MonitoringApplicationService(
            EthKlayWalletService ethKlayHenesisWalletService,
            WalletRepository walletRepository,
            FlushedTransactionRepository flushedTransactionRepository,
            TransactionRepository transactionRepository,
            ActionSupplier<UpdateAction> updateActionSupplier
    ) {
        this.ethKlayHenesisWalletService = ethKlayHenesisWalletService;
        this.walletRepository = walletRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
        this.transactionRepository = transactionRepository;
        this.updateActionSupplier = updateActionSupplier;
    }

    @Transactional
    @Scheduled(fixedRate = 2000, initialDelay = 500)
    public void updateTransactions() {

        List<Transaction> transactions = ethKlayHenesisWalletService.getTransactions(updatedAtGte).getResults()
                .stream()
                .filter(Transaction::isDesired)
                .filter(tx -> transactionRepository.findAllByTransactionId(tx.getTransactionId()).isEmpty())
                .filter(tx -> flushedTransactionRepository.findAllByTransactionId(tx.getTransactionId()).isEmpty())
                .collect(Collectors.toList());


        transactionRepository.saveAll(transactions);
        transactions.forEach(tx -> updateActionSupplier.supply(tx.situation()).updateBalance(tx));

        transactionRepository.findTopByOrderByUpdatedAtDesc().ifPresent(
                gte -> this.updatedAtGte = gte.getUpdatedAt()
        );

    }

    //지갑 상태 업데이트 (CREATING, ACTIVE, INACTIVE)
    @Async
    @Transactional
    @Scheduled(fixedRate = 6000, initialDelay = 2000)
    public void updateWalletStatus() {
        // 모든 사용자 지갑
        ethKlayHenesisWalletService.getAllUserWallet().forEach(u -> {
            walletRepository.findByWalletId(u.getWalletId()).ifPresent(e -> {
                e.setStatus(u.getStatus());
                e.setUpdatedAt(u.getUpdatedAt());
                walletRepository.save(e);
            });
        });
    }

    @Async
    @Transactional
    @Scheduled(fixedRate = 5000, initialDelay = 1000)
    public void updateFlushedTransactionStatus() {

        TransferEvent ethKlayTransferEvent = ethKlayHenesisWalletService.getTransactions(updatedAtGte);

        List<Transaction> transactions = ethKlayTransferEvent.getResults().stream()
                .filter(Transaction::isDesired).collect(Collectors.toList());

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
