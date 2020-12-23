package io.haechi.henesis.assignment.application.btc;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.TransferRepository;
import io.haechi.henesis.assignment.domain.UpdateAction;
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
    private final TransferRepository transferRepository;
    private final ActionSupplier<UpdateAction> updateActionSupplier;
    private String updatedAtGte = Long.toString(System.currentTimeMillis());

    public BtcMonitoringApplicationService(
            @Qualifier("btcHenesisWalletService") BtcHenesisWalletService btcHenesisWalletService,
            TransferRepository transferRepository,
            ActionSupplier<UpdateAction> updateActionSupplier
    ) {
        this.btcHenesisWalletService = btcHenesisWalletService;
        this.transferRepository = transferRepository;
        this.updateActionSupplier = updateActionSupplier;
    }


    @Transactional
    @Scheduled(fixedRate = 2000, initialDelay = 500)
    public void updateTransactions() {
        this.transferRepository.findTopByOrderByUpdatedAtDesc().ifPresent(
                gte -> this.updatedAtGte = gte.getUpdatedAt()
        );
        List<Transfer> transfers = btcHenesisWalletService.getTransactions(updatedAtGte).stream()
                .filter(Transfer::isDesired)
                .filter(tx -> this.transferRepository.findAllByHenesisId(tx.getHenesisId()).isEmpty())
                .collect(Collectors.toList());

        this.transferRepository.saveAll(transfers);

        transfers.forEach(tx -> updateActionSupplier.supply(tx.situation()).updateBalance(tx));
    }
}
