package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.TransferRepository;
import io.haechi.henesis.assignment.domain.UpdateAction;
import io.haechi.henesis.assignment.domain.ethklay.EthKlayWalletService;
import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EthMonitoringApplicationService extends MonitoringApplicationService {

    public EthMonitoringApplicationService(
            @Qualifier("ethHenesisWalletService") EthKlayWalletService ethHenesisWalletService,
            DepositAddressRepository depositAddressRepository,
            FlushedTransactionRepository flushedTransactionRepository,
            TransferRepository transferRepository,
            ActionSupplier<UpdateAction> updateActionSupplier
    ) {
        super(
                ethHenesisWalletService,
                depositAddressRepository,
                flushedTransactionRepository,
                transferRepository,
                updateActionSupplier
        );
    }
}
