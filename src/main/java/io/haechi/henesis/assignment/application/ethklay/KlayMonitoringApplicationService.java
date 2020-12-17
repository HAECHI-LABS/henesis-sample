package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.UpdateAction;
import io.haechi.henesis.assignment.domain.ethklay.*;
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
public class KlayMonitoringApplicationService extends MonitoringApplicationService{

    public KlayMonitoringApplicationService(
            @Qualifier("klayHenesisWalletService") EthKlayWalletService klayHenesisWalletService,
            WalletRepository walletRepository,
            FlushedTransactionRepository flushedTransactionRepository,
            TransactionRepository transactionRepository,
            ActionSupplier<UpdateAction> updateActionSupplier
    ) {
        super(
                klayHenesisWalletService,
                walletRepository,
                flushedTransactionRepository,
                transactionRepository,
                updateActionSupplier
        );
    }

}

