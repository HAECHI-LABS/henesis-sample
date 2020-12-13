package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.ethKlayDomain.Amount;
import io.haechi.henesis.assignment.ethKlayDomain.transaction.Transaction;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Qualifier("klayHenesisWalletService")
public class KlayHenesisWalletService extends HenesisWalletService{

    public KlayHenesisWalletService(
            @Qualifier("restTemplate") RestTemplate restTemplate,
            @Qualifier("klayMasterWalletId") String klayMasterWalletId,
            @Qualifier("klayPassphrase") String klayPassphrase
    ) {
        super(restTemplate,klayMasterWalletId,klayPassphrase,"KLAY");
    }


}
