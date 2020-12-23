package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ActionSupplier;
import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.TransferRepository;
import io.haechi.henesis.assignment.domain.UpdateAction;
import io.haechi.henesis.assignment.domain.ethklay.EthKlayWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KlayMonitoringApplicationService extends MonitoringApplicationService {

    public KlayMonitoringApplicationService(
            @Qualifier("klayHenesisWalletService") EthKlayWalletService klayHenesisWalletService,
            DepositAddressRepository depositAddressRepository,
            TransferRepository transferRepository,
            ActionSupplier<UpdateAction> updateActionSupplier
    ) {
        super(
                klayHenesisWalletService,
                depositAddressRepository,
                transferRepository,
                updateActionSupplier
        );
    }

}

