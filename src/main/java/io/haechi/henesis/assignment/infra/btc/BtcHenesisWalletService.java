package io.haechi.henesis.assignment.infra.btc;

import io.haechi.henesis.assignment.domain.btc.BtcAmount;
import io.haechi.henesis.assignment.domain.btc.BtcTransaction;
import io.haechi.henesis.assignment.domain.btc.BtcWalletService;
import io.haechi.henesis.assignment.domain.btc.DepositAddress;
import io.haechi.henesis.assignment.infra.btc.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Qualifier("btcHenesisWalletService")
public class BtcHenesisWalletService implements BtcWalletService {
    private final RestTemplate restTemplate;
    private final String walletId;
    private final String passphrase;
    private final String btcSize;

    public BtcHenesisWalletService(
            @Qualifier("restTemplate") RestTemplate restTemplate,
            @Qualifier("btcWalletId") String btcWalletId,
            @Qualifier("btcPassphrase") String btcPassphrase,
            @Qualifier("btcSize") String btcSize
    ) {
        this.restTemplate = restTemplate;
        this.walletId = btcWalletId;
        this.passphrase = btcPassphrase;
        this.btcSize = btcSize;
    }


    @Override
    public DepositAddress createDepositAddress(String name) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("name", name);
        //param.add("passphrase",passphrase);

        DepositAddressJsonObject response = restTemplate.postForEntity(
                String.format("/btc/wallets/%s/deposit-addresses", walletId),
                param,
                DepositAddressJsonObject.class
        ).getBody();

        return DepositAddress.of(
                response.getDepositAddressId(),
                response.getName(),
                response.getAddress(),
                response.getPub(),
                response.getCreatedAt()
        );
    }

    @Override
    public BtcAmount getEstimatedFee() {
        GetEstimatedFeeJsonObject response = restTemplate.getForEntity(
                String.format("/btc/wallets/%s/estimated-fee", walletId),
                GetEstimatedFeeJsonObject.class
        ).getBody();

        return BtcAmount.of(response.getEstimatedFee());
    }

    @Override
    public BtcAmount getWalletBalance() {
        List<GetWalletBalanceJsonObject> response = Arrays.asList(
                Objects.requireNonNull(restTemplate.getForEntity(
                        String.format("/btc/wallets/%s/balance", walletId),
                        GetWalletBalanceJsonObject[].class
                ).getBody())
        );

        return BtcAmount.of(response.stream().findFirst().get().getSpendableAmount());
    }

    @Override
    public void transfer(BtcAmount amount, String to) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("amount", amount.toHexString());
        param.add("to", to);
        param.add("passphrase", passphrase);

        BtcTransactionJsonObject response = restTemplate.postForEntity(
                String.format("btc/wallets/%s/transfer/", walletId),
                param,
                BtcTransactionJsonObject.class
        ).getBody();
    }

    @Override
    public List<BtcTransaction> getTransactions(String updatedAtGte) {
        GetTransfersJsonObject response = restTemplate.getForEntity(
                String.format("btc/transfers?updatedAtGte=%s&size=%s", updatedAtGte, btcSize),
                GetTransfersJsonObject.class
        ).getBody();

        return response.getResults().stream().map(t ->
                BtcTransaction.of(
                        t.getWalletId(),
                        t.getFeeAmount(),
                        t.getReceivedAt(),
                        t.getSendTo(),
                        t.getType(),
                        t.getStatus(),
                        BtcAmount.of(t.getAmount()),
                        t.getTransaction().getId(),
                        t.getTransaction().getTransactionHash(),
                        t.getTransaction().getCreatedAt(),
                        t.getTransaction().getUpdatedAt()
                )
        ).collect(Collectors.toList()
        );
    }
}
