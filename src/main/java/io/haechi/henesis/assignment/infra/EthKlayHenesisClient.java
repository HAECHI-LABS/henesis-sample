package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.Balance;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.Coin;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.Pagination;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.infra.dto.CoinDto;
import io.haechi.henesis.assignment.infra.dto.CreateHenesisDepositAddressRequest;
import io.haechi.henesis.assignment.infra.dto.EthKlayTransferDto;
import io.haechi.henesis.assignment.infra.dto.HenesisBalanceDto;
import io.haechi.henesis.assignment.infra.dto.HenesisDepositAddressDto;
import io.haechi.henesis.assignment.infra.dto.HenesisFlushRequest;
import io.haechi.henesis.assignment.infra.dto.HenesisTransferRequest;
import io.haechi.henesis.assignment.infra.dto.ValueTransferEventDto;
import io.haechi.henesis.assignment.support.Utils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EthKlayHenesisClient implements HenesisClient {
    private final RestTemplate restTemplate;
    private final String masterWalletId;
    private final String masterWalletAddress;
    private final String passphrase;
    private final Blockchain blockchain;

    public EthKlayHenesisClient(
            RestTemplate restTemplate,
            String maseterWalletId,
            String masterWalletAddress,
            String passphrase,
            Blockchain blockchain
    ) {
        this.restTemplate = restTemplate;
        this.masterWalletId = maseterWalletId;
        this.masterWalletAddress = masterWalletAddress;
        this.passphrase = passphrase;
        this.blockchain = blockchain;
    }

    @Override
    public DepositAddress createDepositAddress(String name) {
        HenesisDepositAddressDto response = this.restTemplate.postForEntity(
                String.format(
                        "%s/wallets/%s/user-wallets",
                        this.blockchain.toSymbol(),
                        this.masterWalletId
                ),
                new CreateHenesisDepositAddressRequest(
                        name,
                        this.passphrase
                ),
                HenesisDepositAddressDto.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.of(response.getStatus()),
                Blockchain.of(response.getBlockchain()),
                response.getName(),
                response.getAddress(),
                this.masterWalletAddress
        );

    }

    @Override
    public Transfer transfer(String to, String symbol, Amount amount) {
        EthKlayTransferDto response = this.restTemplate.postForEntity(
                String.format(
                        "%s/wallets/%s/transfer",
                        this.blockchain.toSymbol(),
                        this.masterWalletId
                ),
                new HenesisTransferRequest(
                        amount.toHexString(),
                        to,
                        symbol,
                        this.passphrase
                ),
                EthKlayTransferDto.class
        ).getBody();

        return Transfer.transfer(
                response.getId(),
                Transfer.Status.of(response.getStatus()),
                to,
                amount,
                symbol.toUpperCase(),
                Blockchain.of(response.getBlockchain()),
                response.getHash(),
                Utils.toLocalDateTime(response.getCreatedAt())
        );
    }

    // TODO: call web3 estimatedGasPrice
    @Override
    public Amount getEstimatedFee() {
        return null;
    }

    @Override
    public List<Transfer> getLatestTransfersByUpdatedAtGte(LocalDateTime updatedAtGte, int size) {
        Pagination<ValueTransferEventDto> response = this.restTemplate.exchange(
                String.format(
                        "%s/value-transfer-events?masterWalletId=%s&updatedAtGte=%s&size=%s",
                        this.blockchain.toSymbol(),
                        this.masterWalletId,
                        Timestamp.valueOf(updatedAtGte).getTime(),
                        size
                ),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Pagination<ValueTransferEventDto>>() {
                }
        ).getBody();
        return response.getResults().stream()
                .map(result -> Transfer.fromHenesis(
                        result.getId(),
                        result.getTransactionId(),
                        result.getFrom(),
                        result.getTo(),
                        Amount.of(result.getAmount()),
                        Blockchain.of(result.getBlockchain()),
                        Transfer.Status.of(result.getStatus()),
                        result.getCoinSymbol(),
                        Transfer.Type.of(result.getTransferType()),
                        result.getHash(),
                        result.resolveOwner(),
                        Utils.toLocalDateTime(result.getUpdatedAt())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<Transfer> getTransfersByUpdatedAtGte(LocalDateTime updatedAtGte, Pageable pageable) {
        Pagination<ValueTransferEventDto> response = this.restTemplate.exchange(
                String.format(
                        "%s/value-transfer-events?masterWalletId=%s&updatedAtGte=%s&page=%s&size=%s",
                        this.blockchain.toSymbol(),
                        this.masterWalletId,
                        Timestamp.valueOf(updatedAtGte).getTime(),
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                ),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Pagination<ValueTransferEventDto>>() {
                }
        ).getBody();

        return new Pagination<>(
                response.getPagination(),
                response.getResults().stream()
                        .map(result -> Transfer.fromHenesis(
                                result.getId(),
                                result.getTransactionId(),
                                result.getFrom(),
                                result.getTo(),
                                Amount.of(result.getAmount()),
                                Blockchain.of(result.getBlockchain()),
                                Transfer.Status.of(result.getStatus()),
                                result.getCoinSymbol(),
                                Transfer.Type.of(result.getTransferType()),
                                result.getHash(),
                                result.resolveOwner(),
                                Utils.toLocalDateTime(result.getUpdatedAt())
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<Balance> getDepositAddressBalances(DepositAddress depositAddress) {
        List<HenesisBalanceDto> response = Arrays.asList(
                this.restTemplate.getForEntity(
                        String.format(
                                "%s/wallets/%s/user-wallets/%s/balance",
                                this.blockchain.toSymbol(),
                                this.masterWalletId,
                                depositAddress.getHenesisId()
                        ),
                        HenesisBalanceDto[].class
                ).getBody()
        );

        return response.stream()
                .map(res -> Balance.of(
                        depositAddress,
                        res.getSymbol(),
                        Amount.of(res.getAmount())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Pagination<DepositAddress> getDepositAddresses(Pageable pageable) {
        Pagination<HenesisDepositAddressDto> response = this.restTemplate.exchange(
                String.format(
                        "%s/wallets/%s/user-wallets?page=%s&size=%s",
                        this.blockchain.toSymbol(),
                        masterWalletId,
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                ),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Pagination<HenesisDepositAddressDto>>() {
                }
        ).getBody();

        return new Pagination<>(
                response.getPagination(),
                response.getResults().stream()
                        .map(result -> DepositAddress.fromHenesis(
                                result.getId(),
                                DepositAddress.Status.of(result.getStatus()),
                                Blockchain.of(result.getBlockchain()),
                                result.getName(),
                                result.getAddress(),
                                this.masterWalletAddress
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Transfer flush(String symbol, List<String> depositAddressHenesisIds) {
        EthKlayTransferDto response = this.restTemplate.postForEntity(
                String.format(
                        "%s/wallets/%s/flush",
                        this.blockchain.toSymbol(),
                        this.masterWalletId
                ),
                new HenesisFlushRequest(
                        symbol,
                        this.passphrase,
                        depositAddressHenesisIds
                ),
                EthKlayTransferDto.class
        ).getBody();

        return Transfer.flush(
                response.getId(),
                symbol,
                Blockchain.of(response.getBlockchain()),
                Utils.toLocalDateTime(response.getCreatedAt())
        );
    }

    @Override
    public DepositAddress getDepositAddress(String id) {
        HenesisDepositAddressDto response = this.restTemplate.getForEntity(
                String.format(
                        "%s/wallets/%s/user-wallets/%s",
                        this.blockchain.toSymbol(),
                        this.masterWalletId,
                        id
                ),
                HenesisDepositAddressDto.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.of(response.getStatus()),
                Blockchain.of(response.getBlockchain()),
                response.getName(),
                response.getAddress(),
                this.masterWalletAddress
        );
    }

    @Override
    public Coin getCoin(String symbol) {
        CoinDto response = this.restTemplate.getForEntity(
                String.format("%s/coins/%s", this.blockchain.toSymbol(), symbol.toUpperCase()),
                CoinDto.class
        ).getBody();

        return new Coin(
                response.getSymbol(),
                response.getDecimals(),
                this.blockchain
        );
    }

    @Override
    public boolean isSupportedCoin(Blockchain blockchain, String symbol) {
        try {
            this.restTemplate.getForEntity(
                    String.format("%s/coins/%s", this.blockchain.toSymbol(), symbol.toUpperCase()),
                    CoinDto.class
            );
        } catch (HttpClientErrorException e) {
            return !e.getStatusCode().isError();
        }
        return true;
    }

    @Override
    public String getMasterWalletAddress() {
        HenesisDepositAddressDto response = this.restTemplate.getForEntity(
                String.format("%s/wallets/%s", this.blockchain.toSymbol(), this.masterWalletId),
                HenesisDepositAddressDto.class
        ).getBody();

        return response.getAddress();
    }
}
