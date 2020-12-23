package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ethklay.EthKlayWalletService;
import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class EthExchangeApplicationService extends ExchangeApplicationService {


    public EthExchangeApplicationService(
            @Qualifier("ethHenesisWalletService") EthKlayWalletService ethHenesisWalletClient,
            WalletRepository walletRepository,
            FlushedTransactionRepository flushedTransactionRepository
    ) {
        super(ethHenesisWalletClient, walletRepository, flushedTransactionRepository);
    }
}
