package io.haechi.henesis.assignment.infra;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Qualifier("ethHenesisWalletService")
public class EthHenesisWalletService extends HenesisWalletService{

    public EthHenesisWalletService(
            @Qualifier("restTemplate") RestTemplate restTemplate,
            @Qualifier("ethMasterWalletId") String ethMasterWalletId,
            @Qualifier("ethPassphrase") String ethPassphrase
    ) {
        super(restTemplate, ethMasterWalletId, ethPassphrase, "ETH");

    }
}
