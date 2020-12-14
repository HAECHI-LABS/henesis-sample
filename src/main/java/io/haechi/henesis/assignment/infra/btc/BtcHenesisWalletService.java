package io.haechi.henesis.assignment.infra.btc;

import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.Transaction;
import io.haechi.henesis.assignment.domain.btc.*;
import io.haechi.henesis.assignment.infra.btc.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class BtcHenesisWalletService implements BtcHenesisWalletClient {
    private final RestTemplate restTemplate;
    private final String walletId;
    private final String passphrase;

    public BtcHenesisWalletService(
            @Qualifier("restTemplate") RestTemplate restTemplate,
            @Qualifier("btcWalletId") String btcWalletId,
            @Qualifier("btcPassphrase") String btcPassphrase
    ) {
        this.restTemplate = restTemplate;
        this.walletId = btcWalletId;
        this.passphrase = btcPassphrase;
    }


    @Override
    public DepositAddress createDepositAddress(String name) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("name", name);
        //param.add("passphrase",passphrase);

        DepositAddressJsonObject response = restTemplate.postForEntity(
                String.format("/btc/wallets/%s", walletId),
                param,
                DepositAddressJsonObject.class
        ).getBody();


        return null;
    }

    @Override
    public Amount getWalletBalance() {
        List<GetWalletBalanceJsonObject> response = Arrays.asList(
                Objects.requireNonNull(restTemplate.getForEntity(
                        String.format("/btc/wallets/%s/balance", walletId),
                        GetWalletBalanceJsonObject[].class
                ).getBody())
        );

        return Amount.of(response.stream().findFirst().get().getSpendableAmount());
    }

    @Override
    public Wallet getWalletInfo(){
        GetWalletAddressJsonObject response = restTemplate.getForEntity(
                String.format("/btc/wallets/%s/",walletId),
                GetWalletAddressJsonObject.class
        ).getBody();

        assert response != null;
        return Wallet.of(
                response.getId(),
                response.getName(),
                response.getAddress(),
                response.getOrgId(),
                response.getAccountKeyJsonObject().getAddress(),
                response.getAccountKeyJsonObject().getKeyFile(),
                response.getAccountKeyJsonObject().getPub(),
                response.getEncryptionKey(),
                response.getStatus(),
                response.isWhitelistActivated(),
                response.getCreatedAt()
        );
    }

    @Override
    public Transaction transfer(Amount amount, String to) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("amount", amount.toHexString());
        param.add("to", to);

        BtcTransactionJsonObject response = restTemplate.postForEntity(
                String.format("btc/wallets/%s/transfer/", walletId),
                param,
                BtcTransactionJsonObject.class
        ).getBody();

        return null;
    }

    @Override
    public List<BtcTransaction> getTransfers(String updatedAt) {
        GetTransfersJsonObject response = restTemplate.getForEntity(
                String.format("btc/transfers?updatedAtGte={updatedAtGte}&size={size}/"),
                GetTransfersJsonObject.class,
                updatedAt, 50
        ).getBody();
        return null;
    }

    @Override
    public List<DepositAddress> getAllDepositAddress() {
        List<DepositAddressJsonObject> response = Objects.requireNonNull(restTemplate.getForEntity(
                String.format("btc/wallets/%s/deposit-addresses/", walletId),
                GetAllDepositAddressJsonObject.class
        ).getBody()).getResults();

        return null;
    }


    @Override
    public List<DepositAddress> getAllWallet() {
        List<GetWalletAddressJsonObject> response = Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(
                "btc/wallets/",
                GetWalletAddressJsonObject[].class
        ).getBody()));

        return null;
    }
}
