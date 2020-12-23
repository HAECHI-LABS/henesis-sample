package io.haechi.henesis.assignment.application.ethklay;

import io.haechi.henesis.assignment.domain.ethklay.EthKlayWalletService;
import io.haechi.henesis.assignment.domain.ethklay.FlushedTransactionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class KlayExchangeApplicationService extends ExchangeApplicationService {

    public KlayExchangeApplicationService(
            @Qualifier("klayHenesisWalletService") EthKlayWalletService klayHenesisWalletClient,
            WalletRepository walletRepository,
            FlushedTransactionRepository flushedTransactionRepository) {
        super(klayHenesisWalletClient, walletRepository, flushedTransactionRepository);
    }
}
