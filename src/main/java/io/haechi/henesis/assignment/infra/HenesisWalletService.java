package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.application.dto.UserWalletDTO;
import io.haechi.henesis.assignment.domain.UserWallet;
import io.haechi.henesis.assignment.infra.dto.CreateUserWalletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HenesisWalletService {
    private final RestTemplate masterWalletRestTemplate;
    private final RestTemplate restTemplate;
    private final String masterWalletId;
    private final String walletPassphrase;

    public HenesisWalletService(
            @Qualifier("walletClient") RestTemplate restTemplate,
            @Qualifier("masterWalletClient") RestTemplate masterWalletRestTemplate,
            @Qualifier("masterWalletId") String masterWalletId,
            @Qualifier("walletPassphrase") String walletPassphrase
    ){
        this.restTemplate = restTemplate;
        this.masterWalletRestTemplate = masterWalletRestTemplate;
        this.masterWalletId = masterWalletId;
        this.walletPassphrase = walletPassphrase;
    }

    public UserWallet createUserWallet(String walletName){
        UserWalletDTO response = masterWalletRestTemplate.postForEntity(
                "/user-wallets",
                CreateUserWalletRequest.builder()
                        .walletName(walletName)
                        .passphrase(walletPassphrase)
                        .build(),
                UserWalletDTO.class).getBody();

        return UserWallet.builder()
                .walletId(response.getId())
                .walletAddress(response.getAddress())
                .walletName(response.getName())
                .masterWalletId(masterWalletId)
                .blockchain(response.getBlockchain())
                .status(response.getStatus())
                .build();

    }

}
