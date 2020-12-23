package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.ethklay.EthKlayWalletService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class KlayExchangeApplicationService extends ExchangeApplicationService {

    public KlayExchangeApplicationService(
            @Qualifier("klayHenesisWalletService") EthKlayWalletService klayHenesisWalletClient,
            DepositAddressRepository depositAddressRepository) {
        super(klayHenesisWalletClient, depositAddressRepository);
    }
}
