package io.haechi.henesis.assignment.infra.ethklay;

import io.haechi.henesis.assignment.domain.DepositAddress;
import io.haechi.henesis.assignment.domain.Pagination;
import io.haechi.henesis.assignment.domain.Transfer;
import io.haechi.henesis.assignment.domain.ethklay.*;
import io.haechi.henesis.assignment.infra.ethklay.dto.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class HenesisWalletService implements EthKlayWalletService {
    private final RestTemplate restTemplate;
    private final String masterWalletId;
    private final String passphrase;
    private final String ticker;
    private final String size;

    public HenesisWalletService(
            RestTemplate restTemplate,
            String maseterWalletId,
            String passphrase,
            String ticker,
            String size
    ) {
        this.restTemplate = restTemplate;
        this.masterWalletId = maseterWalletId;
        this.passphrase = passphrase;
        this.ticker = ticker;
        this.size = size;
    }


    /**
     * 입출금 내역 조회하기
     *
     * @return
     */
    @Override
    public TransferEvent getTransactions(String updatedAtGte) {
        ValueTransferEventsJsonObject response = restTemplate.getForEntity(
                String.format("%s/value-transfer-events?updatedAtGte=%s&size=%s/"
                        , ticker,
                        updatedAtGte,
                        size),
                ValueTransferEventsJsonObject.class
        ).getBody();


        return TransferEvent.of(
                Pagination.of(
                        response.getPagination().getNextUrl(),
                        response.getPagination().getPreviousUrl(),
                        response.getPagination().getTotalCount()
                ),
                response.getResults().stream().map(t ->
                        Transfer.of(
                        )
                ).collect(Collectors.toList())
        );
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
        param.add("passphrase", passphrase);

        UserWalletJsonObject response = restTemplate.postForEntity(
                String.format("%s/wallets/%s/user-wallets/", ticker, masterWalletId),
                param,
                UserWalletJsonObject.class
        ).getBody();


        return DepositAddress.of(
        );

    }

    /**
     * 코인/토큰 전송하 API Call
     *
     * @param
     */
    @Override
    public Transfer transfer(Amount amount, String to) {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("amount", amount.toHexString());
        param.add("to", to);
        param.add("ticker", ticker);
        param.add("passphrase", passphrase);

        TransactionJsonObject response = restTemplate.postForEntity(
                String.format("%s/wallets/%s/transfer/", ticker, masterWalletId),
                param,
                TransactionJsonObject.class
        ).getBody();

        return Transfer.newInstanceOf(
                response.getId(),
                response.getBlockchain(),
                response.getStatus(),
                response.getCreatedAt()
        );

    }


    /**
     * 마스터 지갑에 속한 UserWallet ID 모두 가져오기
     *
     * @return
     */
    @Override
    public List<String> getDepositAddressIds() {

        List<UserWalletJsonObject> getAllUserWallet = Objects.requireNonNull(restTemplate.getForEntity(
                String.format("%s/wallets/%s/user-wallets?size=%s/", ticker, masterWalletId, "50"),
                GetAllUserWalletJsonObject.class
        ).getBody()).getResults();

        return getAllUserWallet.stream()
                .map(UserWalletJsonObject::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepositAddress> getAllDepositAddresses() {

        List<UserWalletJsonObject> response = Objects.requireNonNull(restTemplate.getForEntity(
                String.format("%s/wallets/%s/user-wallets?size=%s/", ticker, masterWalletId, size),
                GetAllUserWalletJsonObject.class
        ).getBody()).getResults();

        return response.stream().map(u ->
                DepositAddress.of(
                )
        ).collect(Collectors.toList());
    }

    /**
     * 사용자 지갑 잔액을 모두 끌어오기 API Call
     *
     * @param
     * @return Transaction
     */
    @Override
    public Transfer flushAll(List<String> userWalletIds) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("ticker", ticker);
        param.add("passphrase", passphrase);
        param.addAll("userWalletIds", userWalletIds);

        TransactionJsonObject response = restTemplate.postForEntity(
                String.format("%s/wallets/%s/flush/", ticker, masterWalletId),
                param,
                TransactionJsonObject.class
        ).getBody();

        return Transfer.newInstanceOf(
                response.getId(),
                response.getBlockchain(),
                response.getStatus(),
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
    public Amount getMasterWalletBalance() {

        List<MasterWalletBalanceJsonObject> response = Arrays.asList(
                Objects.requireNonNull(restTemplate.getForEntity(
                        String.format("%s/wallets/%s/balance/", ticker, masterWalletId),
                        MasterWalletBalanceJsonObject[].class
                ).getBody())
        );

        return Amount.of(response.stream()
                .filter(symbol -> symbol.getSymbol().equals(ticker)).findFirst().get().getSpendableAmount()
        );
    }

}
