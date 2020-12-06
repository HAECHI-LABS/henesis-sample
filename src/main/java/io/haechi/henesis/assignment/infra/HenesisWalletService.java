package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.*;
import io.haechi.henesis.assignment.domain.transaction.Transaction;
import io.haechi.henesis.assignment.infra.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HenesisWalletService implements Exchange {
    private final RestTemplate masterWalletRestTemplate;
    private final RestTemplate restTemplate;
    private final String masterWalletId;
    private final String passphrase;

    public HenesisWalletService(
            @Qualifier("walletClient") RestTemplate restTemplate,
            @Qualifier("masterWalletClient") RestTemplate masterWalletRestTemplate,
            @Qualifier("masterWalletId") String masterWalletId,
            @Qualifier("passphrase") String passphrase
    ) {
        this.restTemplate = restTemplate;
        this.masterWalletRestTemplate = masterWalletRestTemplate;
        this.masterWalletId = masterWalletId;
        this.passphrase = passphrase;
    }


    /**
     * 입출금 내역 조회하기
     * @return
     */
    @Override
    public List<Transaction> getValueTransferEvents(String updatedAt){
        ValueTransferEventsJsonObject response = restTemplate.getForEntity(
                "/value-transfer-events?updatedAtGte={updatedAtGte}&size={size}",
                ValueTransferEventsJsonObject.class,
                updatedAt,50
        ).getBody();

        assert response != null;

        return response.getResults().stream().map(t ->
                        Transaction.of(
                                t.getId(),
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
                ).collect(Collectors.toList());
    }


    /**
     * 사용자 지갑 생성하기 API Call
     *
     * @return UserWallet
     */
    @Override
    public Wallet createUserWallet(String name) {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("name",name);
        param.add("passphrase",passphrase);

        UserWalletJsonObject response = masterWalletRestTemplate.postForEntity(
                "/user-wallets",
                param,
                UserWalletJsonObject.class
        ).getBody();


        return Wallet.of(
                response.getId(),
                response.getName(),
                response.getAddress(),
                response.getBlockchain(),
                response.getStatus(),
                Amount.of(0.0),
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
    public Transaction transfer(Amount amount, String to, String ticker) {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("amount", amount.toHexString());
        param.add("to",to);
        param.add("ticker",ticker);
        param.add("passphrase",passphrase);

        TransactionJsonObject response = masterWalletRestTemplate.postForEntity(
                "/transfer",
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
     * @return
     */
    @Override
    public List<String> getUserWalletIds() {

        List<UserWalletJsonObject> getAllUserWallet =  Objects.requireNonNull(masterWalletRestTemplate.getForEntity(
                "/user-wallets",
                GetAllUserWalletJsonObject.class
        ).getBody()).getResults();

        return getAllUserWallet.stream()
                .map(UserWalletJsonObject::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Wallet> getAllUserWallet() {

        List<UserWalletJsonObject> response =  Objects.requireNonNull(masterWalletRestTemplate.getForEntity(
                "/user-wallets",
                GetAllUserWalletJsonObject.class
        ).getBody()).getResults();

        return response.stream().map(u ->
                Wallet.of(
                        u.getId(),
                        u.getName(),
                        u.getAddress(),
                        Amount.of(0.0),
                        u.getBlockchain(),
                        u.getStatus(),
                        u.getCreatedAt(),
                        u.getUpdatedAt()
                )
        ).collect(Collectors.toList());
    }

    @Override
    public List<Wallet> getAllMasterWallet(){
        List<MasterWalletJsonObject> response = Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(
                "/wallets",
                MasterWalletJsonObject[].class
        ).getBody()));

        return response.stream().map(m ->
                Wallet.of(
                        m.getId(),
                        m.getName(),
                        m.getAddress(),
                        Amount.of(0.0),
                        m.getBlockchain(),
                        m.getStatus(),
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
    public Transaction flushAll(String ticker, List<String> userWalletIds) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("ticker", ticker);
        param.add("passphrase",passphrase);
        param.addAll("userWalletIds",userWalletIds);

        TransactionJsonObject response = masterWalletRestTemplate.postForEntity(
                "/flush",
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
     * @param ticker
     * @return Optional<MasterWalletBalance>
     */
    @Override
    public Amount getMasterWalletBalance(String ticker) {

        List<MasterWalletBalanceJsonObject> response = Arrays.asList(
                Objects.requireNonNull(masterWalletRestTemplate.getForEntity(
                        "/balance",
                        MasterWalletBalanceJsonObject[].class
                ).getBody())
        );

        return Amount.of(response.stream()
                .filter(symbol -> symbol.getSymbol().equals(ticker)).findFirst().get().getSpendableAmount()
        );
    }


}
