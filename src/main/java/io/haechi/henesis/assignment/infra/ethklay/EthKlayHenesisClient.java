package io.haechi.henesis.assignment.infra.ethklay;

import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.Blockchain;
import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.HenesisClient;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.infra.dto.CreateDepositAddressResponse;
import io.haechi.henesis.assignment.infra.ethklay.dto.EthKlayTransferResponse;
import io.haechi.henesis.assignment.infra.ethklay.dto.MasterWalletBalanceJsonObject;
import io.haechi.henesis.assignment.infra.ethklay.dto.PaginationResponse;
import io.haechi.henesis.assignment.infra.ethklay.dto.ValueTransferEventResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EthKlayHenesisClient implements HenesisClient {
    private final RestTemplate restTemplate;
    private final String masterWalletId;
    private final String passphrase;
    private final String size;
    private final Blockchain blockchain;

    public EthKlayHenesisClient(
            RestTemplate restTemplate,
            String maseterWalletId,
            String passphrase,
            String size,
            Blockchain blockchain
    ) {
        this.restTemplate = restTemplate;
        this.masterWalletId = maseterWalletId;
        this.passphrase = passphrase;
        this.size = size;
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

        CreateDepositAddressResponse response = this.restTemplate.postForEntity(
                String.format(
                        "%s/wallets/%s/user-wallets/",
                        this.blockchain.toSymbol(),
                        this.masterWalletId
                ),
                param,
                CreateDepositAddressResponse.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.of(response.getStatus()),
                Blockchain.of(response.getBlockchain()),
                response.getName(),
                response.getAddress()
        );

    }

    /**
     * 코인/토큰 전송하기 API Call
     *
     * @param
     */
    @Override
    // TODO: support token
    public Transfer transfer(String to, Amount amount) {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("amount", amount.toHexString());
        param.add("to", to);
        param.add("ticker", this.blockchain.toSymbol());
        param.add("passphrase", this.passphrase);

        // TODO: template
        EthKlayTransferResponse response = this.restTemplate.postForEntity(
                String.format(
                        "%s/wallets/%s/transfer",
                        this.blockchain.toSymbol(),
                        this.masterWalletId
                ),
                param,
                EthKlayTransferResponse.class
        ).getBody();

        return Transfer.transfer(
                response.getId(),
                Transfer.Status.of(response.getStatus()),
                Blockchain.of(response.getBlockchain()),
                response.getCreatedAt()
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

        List<MasterWalletBalanceJsonObject> response = Arrays.asList(
                this.restTemplate.getForEntity(
                        String.format(
                                "%s/wallets/%s/balance",
                                this.blockchain.toSymbol(),
                                this.masterWalletId
                        ),
                        MasterWalletBalanceJsonObject[].class
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
    public List<Transfer> getLatestTransfersByUpdatedAtGte(String updatedAtGte) {
        PaginationResponse<ValueTransferEventResponse> response = this.restTemplate.getForEntity(
                String.format(
                        "%s/value-transfer-events?updatedAtGte=%s&size=%s",
                        this.blockchain.toSymbol(),
                        updatedAtGte,
                        this.size
                ),
                PaginationResponse.class
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
                        result.getUpdatedAt()
                ))
                .collect(Collectors.toList());
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

        EthKlayTransferResponse response = this.restTemplate.postForEntity(
                String.format(
                        "%s/wallets/%s/flush",
                        this.blockchain.toSymbol(),
                        this.masterWalletId
                ),
                param,
                EthKlayTransferResponse.class
        ).getBody();

        return Transfer.newInstanceOf(
                response.getId(),
                response.getBlockchain(),
                response.getStatus(),
                response.getCreatedAt()
        );
    }

    @Override
    public DepositAddress getDepositAddress(String id) {
        CreateDepositAddressResponse response = this.restTemplate.getForEntity(
                String.format("%s/wallets/%s/user-wallets/%s", this.blockchain.toSymbol(), masterWalletId, id),
                CreateDepositAddressResponse.class
        ).getBody();

        return DepositAddress.fromHenesis(
                response.getId(),
                DepositAddress.Status.of(response.getStatus()),
                Blockchain.of(response.getBlockchain()),
                response.getName(),
                response.getAddress()
        );
    }
}
