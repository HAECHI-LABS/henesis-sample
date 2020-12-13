package io.haechi.henesis.assignment.ethKlayApplication;

import io.haechi.henesis.assignment.ethKlayDomain.EthKlayHenesisWalletClient;
import io.haechi.henesis.assignment.ethKlayDomain.FlushedTransactionRepository;
import io.haechi.henesis.assignment.ethKlayDomain.WalletRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class KlayExchangeApplicationService extends ExchangeApplicationService{

    public KlayExchangeApplicationService(
            @Qualifier("klayHenesisWalletService") EthKlayHenesisWalletClient ethKlayHenesisWalletClient,
            WalletRepository walletRepository,
            FlushedTransactionRepository flushedTransactionRepository) {
        super(ethKlayHenesisWalletClient, walletRepository, flushedTransactionRepository);
    }
}
