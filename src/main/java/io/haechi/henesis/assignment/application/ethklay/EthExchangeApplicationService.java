package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.DepositAddressRepository;
import io.haechi.henesis.assignment.domain.ethklay.EthKlayWalletService;
import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class EthExchangeApplicationService extends ExchangeApplicationService {


    public EthExchangeApplicationService(
            @Qualifier("ethHenesisWalletService") EthKlayWalletService ethHenesisWalletClient,
            DepositAddressRepository depositAddressRepository,
            FlushedTransactionRepository flushedTransactionRepository
    ) {
        super(ethHenesisWalletClient, depositAddressRepository, flushedTransactionRepository);
    }
}
