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
import io.haechi.henesis.assignment.infra.dto.CreateDepositAddressDto;
import io.haechi.henesis.assignment.infra.dto.EthKlayTransferDto;
import io.haechi.henesis.assignment.infra.dto.MasterWalletBalanceDto;
import io.haechi.henesis.assignment.infra.dto.ValueTransferEventDto;
import io.haechi.henesis.assignment.support.Utils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EthKlayHenesisClient implements HenesisClient {
    private final RestTemplate restTemplate;
    private final String masterWalletId;
    private final String passphrase;
    private final Blockchain blockchain;

    public EthKlayHenesisClient(
            RestTemplate restTemplate,
            String maseterWalletId,
            String passphrase,
            Blockchain blockchain
    ) {
        this.restTemplate = restTemplate;
        this.masterWalletId = maseterWalletId;
        this.passphrase = passphrase;
        this.blockchain = blockchain;
    }

    /**
     * 사용자 지갑 생성하기 API Call
     *
     * @return UserWallet
     */
    @Override
    public DepositAddress createDepositAddress(String name) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("name", name);
        param.add("passphrase", this.passphrase);

        CreateDepositAddressDto response = this.restTemplate.postForEntity(
                String.format(
                        "%s/wallets/%s/user-wallets",
                        this.blockchain.toSymbol(),
                        this.masterWalletId
                ),
                param,
                CreateDepositAddressDto.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.of(response.getStatus()),
                Blockchain.of(response.getBlockchain()),
                response.getName(),
                response.getAddress(),
                this.getMasterWalletAddress()
        );

    }

    /**
     * 코인/토큰 전송하기 API Call
     *
     * @param
     */
    @Override
    // TODO: support token
    public Transfer transfer(String to, String symbol, Amount amount) {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("amount", amount.toHexString());
        param.add("to", to);
        param.add("ticker", symbol);
        param.add("passphrase", this.passphrase);

        // TODO: template
        EthKlayTransferDto response = this.restTemplate.postForEntity(
                String.format(
                        "%s/wallets/%s/transfer",
                        this.blockchain.toSymbol(),
                        this.masterWalletId
                ),
                param,
                EthKlayTransferDto.class
        ).getBody();

        return Transfer.transfer(
                response.getId(),
                Transfer.Status.of(response.getStatus()),
                to,
                amount,
                response.getCoinSymbol(),
                Blockchain.of(response.getBlockchain()),
                response.getHash(),
                Utils.toLocalDateTime(response.getCreatedAt())
        );
    }

    /**
     * 마스터 지갑 잔고 조회하기 API Call
     * 요청한 ticker 에 맞는 마스터 지갑 잔고를 조회힙니다.
     *
     * @return Optional<MasterWalletBalance>
     */
    @Override
    // TODO: polish
    public Amount getMasterWalletBalance() {

        List<MasterWalletBalanceDto> response = Arrays.asList(
                this.restTemplate.getForEntity(
                        String.format(
                                "%s/wallets/%s/balance",
                                this.blockchain.toSymbol(),
                                this.masterWalletId
                        ),
                        MasterWalletBalanceDto[].class
                ).getBody()
        );

        return Amount.of(response.stream()
                .filter(symbol -> symbol.getSymbol().equals(this.blockchain.toSymbol())).findFirst().get().getSpendableAmount()
        );
    }

    // TODO: call web3 estimatedGasPrice
    @Override
    public Amount getEstimatedFee() {
        return null;
    }

    /**
     * 입출금 내역 조회하기
     *
     * @return
     */
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
                        result.getFrom(),
                        result.getTo(),
                        Amount.of(result.getAmount()),
                        Blockchain.of(result.getBlockchain()),
                        Transfer.Status.of(result.getStatus()),
                        result.getCoinSymbol(),
                        Transfer.Type.of(result.getTransferType()),
                        result.getHash(),
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
                                result.getFrom(),
                                result.getTo(),
                                Amount.of(result.getAmount()),
                                Blockchain.of(result.getBlockchain()),
                                Transfer.Status.of(result.getStatus()),
                                result.getCoinSymbol(),
                                Transfer.Type.of(result.getTransferType()),
                                result.getHash(),
                                Utils.toLocalDateTime(result.getUpdatedAt())
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public List<Balance> getDepositAddressBalances(DepositAddress depositAddress) {
        List<MasterWalletBalanceDto> response = Arrays.asList(
                this.restTemplate.getForEntity(
                        String.format(
                                "%s/wallets/%s/user-wallets/%s/balance",
                                this.blockchain.toSymbol(),
                                this.masterWalletId,
                                depositAddress.getHenesisId()
                        ),
                        MasterWalletBalanceDto[].class
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

    // TODO: dto
    @Override
    public Pagination<DepositAddress> getDepositAddresses(Pageable pageable) {
        Pagination<CreateDepositAddressDto> response = this.restTemplate.exchange(
                String.format(
                        "%s/wallets/%s/user-wallets?page=%s&size=%s",
                        this.blockchain.toSymbol(),
                        masterWalletId,
                        pageable.getPageNumber(),
                        pageable.getPageSize()
                ),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Pagination<CreateDepositAddressDto>>() {
                }
        ).getBody();

        String masterWalletAddress = this.getMasterWalletAddress();
        return new Pagination<>(
                response.getPagination(),
                response.getResults().stream()
                        .map(result -> DepositAddress.fromHenesis(
                                result.getId(),
                                DepositAddress.Status.of(result.getStatus()),
                                Blockchain.of(result.getBlockchain()),
                                result.getName(),
                                result.getAddress(),
                                masterWalletAddress
                        ))
                        .collect(Collectors.toList())
        );
    }

    /**
     * 사용자 지갑 잔액을 모두 끌어오기 API Call
     *
     * @param
     * @return Transaction
     */
    @Override
    public Transfer flush(List<String> depositAddressIds) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("ticker", this.blockchain.toSymbol());
        param.add("passphrase", this.passphrase);
        param.addAll("userWalletIds", depositAddressIds);

        EthKlayTransferDto response = this.restTemplate.postForEntity(
                String.format(
                        "%s/wallets/%s/flush",
                        this.blockchain.toSymbol(),
                        this.masterWalletId
                ),
                param,
                EthKlayTransferDto.class
        ).getBody();

        return Transfer.flush(
                response.getId(),
                response.getCoinSymbol(),
                Blockchain.of(response.getBlockchain()),
                Transfer.Status.of(response.getStatus()),
                Utils.toLocalDateTime(response.getCreatedAt())
        );
    }

    @Override
    public DepositAddress getDepositAddress(String id) {
        CreateDepositAddressDto response = this.restTemplate.getForEntity(
                String.format(
                        "%s/wallets/%s/user-wallets/%s",
                        this.blockchain.toSymbol(),
                        this.masterWalletId,
                        id
                ),
                CreateDepositAddressDto.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.of(response.getStatus()),
                Blockchain.of(response.getBlockchain()),
                response.getName(),
                response.getAddress(),
                this.getMasterWalletAddress()
        );
    }

    @Override
    public Coin getCoin(String symbol) {
        CoinDto response = this.restTemplate.getForEntity(
                String.format("%s/coins/%s", this.blockchain.toSymbol(), symbol.toLowerCase()),
                CoinDto.class
        ).getBody();

        return new Coin(
                response.getSymbol(),
                response.getDecimals(),
                this.blockchain
        );
    }

    @Override
    public String getMasterWalletAddress() {
        CreateDepositAddressDto response = this.restTemplate.getForEntity(
                String.format("%s/wallets/%s", this.blockchain.toSymbol(), this.masterWalletId),
                CreateDepositAddressDto.class
        ).getBody();

        return response.getAddress();
    }
}
