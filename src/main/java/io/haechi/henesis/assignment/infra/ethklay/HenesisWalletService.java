package io.haechi.henesis.assignment.infra.ethklay;

import io.haechi.henesis.assignment.domain.Pagination;
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
                        Transaction.of(
                                t.getFrom(),
                                t.getTo(),
                                t.getAmount(),
                                t.getBlockchain(),
                                t.getStatus(),
                                t.getTransactionId(),
                                t.getTransactionHash(),
                                t.getCoinSymbol(),
                                t.getConfirmation(),
                                t.getTransferType(),
                                t.getCreatedAt(),
                                t.getUpdatedAt(),
                                t.getWalletId(),
                                t.getWalletName()
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
    public Wallet createUserWallet(String name) {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("name", name);
        param.add("passphrase", passphrase);

        UserWalletJsonObject response = restTemplate.postForEntity(
                String.format("%s/wallets/%s/user-wallets/", ticker, masterWalletId),
                param,
                UserWalletJsonObject.class
        ).getBody();


        return Wallet.of(
                response.getId(),
                response.getName(),
                response.getAddress(),
                response.getBlockchain(),
                response.getStatus(),
                masterWalletId,
                response.getCreatedAt(),
                response.getUpdatedAt()
        );

    }

    /**
     * 코인/토큰 전송하 API Call
     *
     * @param
     */
    @Override
    public Transaction transfer(Amount amount, String to) {

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

        return Transaction.newInstanceOf(
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
    public List<String> getUserWalletIds() {

        List<UserWalletJsonObject> getAllUserWallet = Objects.requireNonNull(restTemplate.getForEntity(
                String.format("%s/wallets/%s/user-wallets/", ticker, masterWalletId),
                GetAllUserWalletJsonObject.class
        ).getBody()).getResults();

        return getAllUserWallet.stream()
                .map(UserWalletJsonObject::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Wallet> getAllUserWallet() {

        List<UserWalletJsonObject> response = Objects.requireNonNull(restTemplate.getForEntity(
                String.format("%s/wallets/%s/user-wallets/", ticker, masterWalletId),
                GetAllUserWalletJsonObject.class
        ).getBody()).getResults();

        return response.stream().map(u ->
                Wallet.of(
                        u.getId(),
                        u.getName(),
                        u.getAddress(),
                        u.getBlockchain(),
                        u.getStatus(),
                        masterWalletId,
                        u.getCreatedAt(),
                        u.getUpdatedAt()
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<Wallet> getAllMasterWallet() {
        List<MasterWalletJsonObject> response = Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(
                String.format("%s/wallets/", ticker),
                MasterWalletJsonObject[].class
        ).getBody()));

        return response.stream().map(m ->
                Wallet.of(
                        m.getId(),
                        m.getName(),
                        m.getAddress(),
                        m.getBlockchain(),
                        m.getStatus(),
                        masterWalletId,
                        m.getCreatedAt(),
                        m.getUpdatedAt()
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
    public Transaction flushAll(List<String> userWalletIds) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("ticker", ticker);
        param.add("passphrase", passphrase);
        param.addAll("userWalletIds", userWalletIds);

        TransactionJsonObject response = restTemplate.postForEntity(
                String.format("%s/wallets/%s/flush/", ticker, masterWalletId),
                param,
                TransactionJsonObject.class
        ).getBody();

        return Transaction.newInstanceOf(
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
