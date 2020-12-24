package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.infra.dto.BtcTransferDto;
import io.haechi.henesis.assignment.infra.dto.GetEstimatedFeeDto;
import io.haechi.henesis.assignment.infra.dto.GetWalletBalanceDto;
import io.haechi.henesis.assignment.infra.dto.CreateDepositAddressDto;
import io.haechi.henesis.assignment.infra.dto.PaginationDto;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BtcHenesisClient implements HenesisClient {
    private final RestTemplate restTemplate;
    private final String masterWalletId;
    private final String passphrase;
    private final String btcSize;

    public BtcHenesisClient(
            RestTemplate restTemplate,
            String btcMasterWalletId,
            String btcPassphrase,
            String btcSize
    ) {
        this.restTemplate = restTemplate;
        this.masterWalletId = btcMasterWalletId;
        this.passphrase = btcPassphrase;
        this.btcSize = btcSize;
    }


    @Override
    public DepositAddress createDepositAddress(String name) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("name", name);

        CreateDepositAddressDto response = restTemplate.postForEntity(
                String.format("/btc/wallets/%s/deposit-addresses", masterWalletId),
                param,
                CreateDepositAddressDto.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.ACTIVE,
                Blockchain.BITCOIN,
                response.getName(),
                response.getAddress()
        );
    }

    @Override
    public Amount getEstimatedFee() {
        GetEstimatedFeeDto response = restTemplate.getForEntity(
                String.format("/btc/wallets/%s/estimated-fee", masterWalletId),
                GetEstimatedFeeDto.class
        ).getBody();

        return Amount.of(response.getEstimatedFee());
    }

    @Override
    public Amount getMasterWalletBalance() {
        List<GetWalletBalanceDto> response = Arrays.asList(
                Objects.requireNonNull(restTemplate.getForEntity(
                        String.format("/btc/wallets/%s/balance", masterWalletId),
                        GetWalletBalanceDto[].class
                ).getBody())
        );

        return Amount.of(response.stream().findFirst().get().getSpendableAmount());
    }

    @Override
    public Transfer transfer(String to, String symbol, Amount amount) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("amount", amount.toHexString());
        param.add("to", to);
        param.add("passphrase", this.passphrase);

        BtcTransferDto response = this.restTemplate.postForEntity(
                String.format("btc/wallets/%s/transfer", this.masterWalletId),
                param,
                BtcTransferDto.class
        ).getBody();

        return Transfer.transfer(
                response.getId(),
                Transfer.Status.of(response.getStatus()),
                to,
                amount,
                symbol,
                Blockchain.BITCOIN,
                response.getHash(),
                response.getUpdatedAt()
        );
    }

    @Override
    public List<Transfer> getLatestTransfersByUpdatedAtGte(String updatedAtGte) {
        PaginationDto<BtcTransferDto> response = restTemplate.getForEntity(
                String.format("btc/transfers?updatedAtGte=%s&size=%s", updatedAtGte, this.btcSize),
                PaginationDto.class
        ).getBody();

        return response.getResults().stream()
                .map(result -> Transfer.fromHenesis(
                        result.getId(),
                        null,
                        result.getReceivedAt() != null
                                ? result.getReceivedAt()
                                : result.getSendTo(),
                        Amount.of(result.getAmount()),
                        Blockchain.BITCOIN,
                        Transfer.Status.of(result.getStatus()),
                        "BTC",
                        Transfer.Type.of(result.getType()),
                        result.getHash(),
                        result.getUpdatedAt()
                )).collect(Collectors.toList()
                );
    }

    @Override
    public Transfer flush(List<String> depositAddressIds) {
        throw new IllegalStateException("henesis doesn't support flush for bitcoin");
    }

    @Override
    public DepositAddress getDepositAddress(String id) {
        CreateDepositAddressDto response = restTemplate.getForEntity(
                String.format("btc/wallets/%s/deposit-addresses/%s", this.masterWalletId, id),
                CreateDepositAddressDto.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.ACTIVE,
                Blockchain.BITCOIN,
                response.getName(),
                response.getAddress()
        );
    }
}
