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

    private String updatedAtGte = Long.toString(System.currentTimeMillis());
    private final String updatedAtLt = "";

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
        btcTransactionRepository.findTopByOrderByUpdatedAtDesc().ifPresent(
                gte -> this.updatedAtGte = gte.getUpdatedAt()
        );
        List<BtcTransaction> btcTransactions = btcHenesisWalletService.getTransactions(updatedAtGte).stream()
                .filter(BtcTransaction::isDesired)
                .filter(tx -> btcTransactionRepository.findAllByTransactionId(tx.getTransactionId()).isEmpty())
                .collect(Collectors.toList());

        btcTransactionRepository.saveAll(btcTransactions);

        btcTransactions.forEach(tx -> updateActionSupplier.supply(tx.situation()).updateBalance(tx));


//        btcTransactionRepository.findTopByOrderByUpdatedAtDesc().ifPresent(
//                lt -> this.updatedAtLt = lt.getUpdatedAt()
//        );


//      newTransaction.forEach(tx -> updateActionSupplier.supply(tx.situation()).updateBalance(tx));


    }

}
