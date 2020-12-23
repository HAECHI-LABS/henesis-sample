package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.UpdateAction;
import io.haechi.henesis.assignment.domain.ethklay.EthKlayWalletService;
import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import io.haechi.henesis.assignment.domain.ethklay.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EthMonitoringApplicationService extends MonitoringApplicationService {

    public EthMonitoringApplicationService(
            @Qualifier("ethHenesisWalletService") EthKlayWalletService ethHenesisWalletService,
            WalletRepository walletRepository,
            FlushedTransactionRepository flushedTransactionRepository,
            TransactionRepository transactionRepository,
            ActionSupplier<UpdateAction> updateActionSupplier
    ) {
        super(
                ethHenesisWalletService,
                walletRepository,
                flushedTransactionRepository,
                transactionRepository,
                updateActionSupplier
        );
    }
}
