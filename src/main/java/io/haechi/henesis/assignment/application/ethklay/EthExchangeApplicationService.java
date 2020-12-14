package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ethklay.EthKlayHenesisWalletClient;
import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import io.haechi.henesis.assignment.domain.ethklay.WalletRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class EthExchangeApplicationService extends ExchangeApplicationService {


    public EthExchangeApplicationService(
            @Qualifier("ethHenesisWalletService") EthKlayHenesisWalletClient ethKlayHenesisWalletClient,
            WalletRepository walletRepository,
            FlushedTransactionRepository flushedTransactionRepository
    ) {
        super(ethKlayHenesisWalletClient, walletRepository, flushedTransactionRepository);
    }
}
