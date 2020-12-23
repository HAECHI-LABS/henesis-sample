package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.TransferRepository;
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
    private final DepositAddressRepository depositAddressRepository;
    private final FlushedTransactionRepository flushedTransactionRepository;
    private final TransferRepository transferRepository;
    private final ActionSupplier<UpdateAction> updateActionSupplier;

    private String updatedAtGte = Long.toString(System.currentTimeMillis());

    public MonitoringApplicationService(
            EthKlayWalletService ethKlayHenesisWalletService,
            DepositAddressRepository depositAddressRepository,
            FlushedTransactionRepository flushedTransactionRepository,
            TransferRepository transferRepository,
            ActionSupplier<UpdateAction> updateActionSupplier
    ) {
        this.ethKlayHenesisWalletService = ethKlayHenesisWalletService;
        this.depositAddressRepository = depositAddressRepository;
        this.flushedTransactionRepository = flushedTransactionRepository;
        this.transferRepository = transferRepository;
        this.updateActionSupplier = updateActionSupplier;
    }

    @Transactional
    @Scheduled(fixedRate = 2000, initialDelay = 500)
    public void updateTransactions() {

        List<Transfer> transactions = ethKlayHenesisWalletService.getTransactions(updatedAtGte).getResults()
                .stream()
                .filter(Transfer::isDesired)
                .filter(tx -> this.transferRepository.findAllByHenesisId(tx.getHenesisId()).isEmpty())
                .filter(tx -> flushedTransactionRepository.findAllByTransactionId(tx.getHenesisId()).isEmpty())
                .collect(Collectors.toList());

        this.transferRepository.saveAll(transactions);
        transactions.forEach(tx -> updateActionSupplier.supply(tx.situation()).updateBalance(tx));

        this.transferRepository.findTopByOrderByUpdatedAtDesc().ifPresent(
                gte -> this.updatedAtGte = gte.getUpdatedAt()
        );

    }

    //지갑 상태 업데이트 (CREATING, ACTIVE, INACTIVE)
    @Async
    @Transactional
    @Scheduled(fixedRate = 6000, initialDelay = 2000)
    public void updateWalletStatus() {
        // 모든 사용자 지갑
        ethKlayHenesisWalletService.getAllDepositAddresses().forEach(u -> {
            this.depositAddressRepository.findByAddress(u.getAddress()).ifPresent(e -> {
                e.setStatus(u.getStatus());
                e.setUpdatedAt(u.getUpdatedAt());
                this.depositAddressRepository.save(e);
            });
        });
    }

    @Async
    @Transactional
    @Scheduled(fixedRate = 5000, initialDelay = 1000)
    public void updateFlushedTransactionStatus() {

        TransferEvent ethKlayTransferEvent = ethKlayHenesisWalletService.getTransactions(updatedAtGte);

        List<Transfer> transactions = ethKlayTransferEvent.getResults().stream()
                .filter(Transfer::isDesired).collect(Collectors.toList());

        transactions.forEach(tx -> {
            flushedTransactionRepository.findByTransactionId(tx.getHenesisId())
                    .ifPresent(f -> {
                        f.setStatus(tx.getStatus());
                        f.setUpdatedAt(tx.getUpdatedAt());
                        flushedTransactionRepository.save(f);
                    });
        });
    }
}
