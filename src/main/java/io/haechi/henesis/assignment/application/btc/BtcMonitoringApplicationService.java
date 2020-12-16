package io.haechi.henesis.assignment.application.btc;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.UpdateAction;
import io.haechi.henesis.assignment.domain.btc.BtcTransaction;
import io.haechi.henesis.assignment.domain.btc.BtcTransactionRepository;
import io.haechi.henesis.assignment.infra.btc.BtcHenesisWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BtcMonitoringApplicationService {
    private final BtcHenesisWalletService btcHenesisWalletService;
    private final BtcTransactionRepository btcTransactionRepository;
    private final ActionSupplier<UpdateAction> updateActionSupplier;

    private String updatedAt = Long.toString(System.currentTimeMillis());

    public BtcMonitoringApplicationService(
            @Qualifier("btcHenesisWalletService") BtcHenesisWalletService btcHenesisWalletService,
            BtcTransactionRepository btcTransactionRepository,
            ActionSupplier<UpdateAction> updateActionSupplier
    ) {
        this.btcHenesisWalletService = btcHenesisWalletService;
        this.btcTransactionRepository = btcTransactionRepository;
        this.updateActionSupplier = updateActionSupplier;
    }


    @Transactional
    @Scheduled(fixedRate = 2000, initialDelay = 500)
    public void updateTransactions() {

        List<BtcTransaction> btcTransactions = btcHenesisWalletService.getTransactions(updatedAt).stream()
                .filter(BtcTransaction::isDesired)
                .collect(Collectors.toList());

        List<BtcTransaction> newTransaction = btcTransactions.stream()
                .filter(tx -> btcTransactionRepository.findAllByTransactionId(tx.getTransactionId()).isEmpty())
                .collect(Collectors.toList());
        btcTransactionRepository.saveAll(newTransaction);
        newTransaction.forEach(tx -> updateActionSupplier.supply(tx.situation()).updateBalance(tx));

        btcTransactionRepository.findTopByOrderByUpdatedAtDesc().ifPresent(u -> {
            this.updatedAt = u.getUpdatedAt();
        });

    }

}
