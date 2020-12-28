package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.Balance;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.Coin;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.Pagination;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.exception.InternalServerException;
import io.haechi.henesis.assignment.infra.dto.BtcTransferDto;
import io.haechi.henesis.assignment.infra.dto.CreateHenesisDepositAddressRequest;
import io.haechi.henesis.assignment.infra.dto.GetEstimatedFeeDto;
import io.haechi.henesis.assignment.infra.dto.HenesisDepositAddressDto;
import io.haechi.henesis.assignment.infra.dto.HenesisTransferRequest;
import io.haechi.henesis.assignment.support.Utils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BtcHenesisClient implements HenesisClient {
    private final RestTemplate restTemplate;
    private final String masterWalletId;
    private final String masterWalletAddress;
    private final String passphrase;

    public BtcHenesisClient(
            RestTemplate restTemplate,
            String masterWalletId,
            String masterWalletAddress,
            String paassphrase
    ) {
        this.restTemplate = restTemplate;
        this.masterWalletId = masterWalletId;
        this.masterWalletAddress = masterWalletAddress;
        this.passphrase = paassphrase;
    }


    @Override
    public DepositAddress createDepositAddress(String name) {
        HenesisDepositAddressDto response = restTemplate.postForEntity(
                String.format("/btc/wallets/%s/deposit-addresses", masterWalletId),
                new CreateHenesisDepositAddressRequest(name),
                HenesisDepositAddressDto.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.ACTIVE,
                Blockchain.BITCOIN,
                response.getName(),
                response.getAddress(),
                this.masterWalletAddress
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
    public Transfer transfer(String to, String symbol, Amount amount) {
        BtcTransferDto response = this.restTemplate.postForEntity(
                String.format("btc/wallets/%s/transfer", this.masterWalletId),
                new HenesisTransferRequest(
                        amount.toHexString(),
                        to,
                        this.passphrase
                ),
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
                Utils.toLocalDateTime(response.getCreatedAt())
        );
    }

    @Override
    public List<Transfer> getLatestTransfersByUpdatedAtGte(LocalDateTime updatedAtGte, int size) {
        Pagination<BtcTransferDto> response = restTemplate.exchange(
                String.format(
                        "btc/transfers?updatedAtGte=%s&size=%s",
                        Timestamp.valueOf(updatedAtGte).getTime(),
                        size
                ),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Pagination<BtcTransferDto>>() {
                }
        ).getBody();

        return response.getResults().stream()
                .map(result -> Transfer.fromHenesis(
                        result.getId(),
                        result.getTransaction().getId(),
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
                        Utils.toLocalDateTime(result.getCreatedAt())
                )).collect(Collectors.toList());
    }

    @Override
    public Pagination<Transfer> getTransfersByUpdatedAtGte(LocalDateTime updatedAtGte, Pageable pageable) {
        Pagination<BtcTransferDto> response = this.restTemplate.exchange(
                String.format(
                        "btc/transfers?walletId=%s&updatedAtGte=%s&page=%s&size=%s",
                        this.masterWalletId,
                        Timestamp.valueOf(updatedAtGte).getTime(),
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                ),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Pagination<BtcTransferDto>>() {
                }
        ).getBody();

        return new Pagination<>(
                response.getPagination(),
                response.getResults().stream()
                        .map(result -> Transfer.fromHenesis(
                                result.getId(),
                                result.getTransaction().getId(),
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
                                Utils.toLocalDateTime(result.getCreatedAt())
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<Balance> getDepositAddressBalances(DepositAddress depositAddress) {
        throw new InternalServerException("henesis doesn't manage deposit address's balance for bitcoin");
    }

    @Override
    public Pagination<DepositAddress> getDepositAddresses(Pageable pageable) {
        Pagination<HenesisDepositAddressDto> response = this.restTemplate.exchange(
                String.format(
                        "btc/wallets/%s/deposit-addresses?page=%s&size=%s",
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
                                DepositAddress.Status.ACTIVE,
                                Blockchain.BITCOIN,
                                result.getName(),
                                result.getAddress(),
                                this.masterWalletAddress
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Transfer flush(List<String> depositAddressHenesisIds) {
        throw new InternalServerException("henesis doesn't support flush for bitcoin");
    }

    @Override
    public DepositAddress getDepositAddress(String id) {
        HenesisDepositAddressDto response = restTemplate.getForEntity(
                String.format("btc/wallets/%s/deposit-addresses/%s", this.masterWalletId, id),
                HenesisDepositAddressDto.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.ACTIVE,
                Blockchain.BITCOIN,
                response.getName(),
                response.getAddress(),
                this.masterWalletAddress
        );
    }

    @Override
    public Coin getCoin(String symbol) {
        return new Coin(
                Blockchain.BITCOIN.toSymbol(),
                8,
                Blockchain.BITCOIN
        );
    }

    @Override
    public boolean isSupportedCoin(Blockchain blockchain, String symbol) {
        return blockchain.equals(Blockchain.BITCOIN) && symbol.equalsIgnoreCase(Blockchain.BITCOIN.toSymbol());
    }

    @Override
    public String getMasterWalletAddress() {
        HenesisDepositAddressDto response = this.restTemplate.getForEntity(
                String.format("btc/wallets/%s", this.masterWalletId),
                HenesisDepositAddressDto.class
        ).getBody();

        return response.getAddress();
    }
}
