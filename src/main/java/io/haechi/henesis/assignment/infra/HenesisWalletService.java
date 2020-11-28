package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.Amount;
import io.haechi.henesis.assignment.domain.Transaction;
import io.haechi.henesis.assignment.domain.UserWallet;
import io.haechi.henesis.assignment.domain.WalletService;
import io.haechi.henesis.assignment.infra.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HenesisWalletService implements WalletService {
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
     * 사용자 지갑 생성하기 API Call
     *
     * @return UserWallet
     */
    @Override
    public UserWallet createUserWallet(String name) {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("name",name);
        param.add("passphrase",passphrase);

        UserWalletJsonObject response = masterWalletRestTemplate.postForEntity(
                "/user-wallets",
                param,
                UserWalletJsonObject.class).getBody();


        return UserWallet.builder()
                .walletId(response.getId())
                .walletAddress(response.getAddress())
                .walletName(response.getName())
                .masterWalletId(masterWalletId)
                .blockchain(response.getBlockchain())
                .status(response.getStatus())
                .build();

    }

    /**
     * 코인/토큰 조회하기 API Call
     *
     * @param
     * @return Transaction
     */
    @Override
    public Transaction transfer(Amount amount,
                                String to,
                                String ticker) {

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        param.add("amount", amount.toHexString());
        param.add("to",to);
        param.add("ticker",ticker);
        param.add("passphrase",passphrase);

        TransactionJsonObject response = masterWalletRestTemplate.postForEntity(
                "/transfer",
                param,
                TransactionJsonObject.class).getBody();

        return Transaction.builder()
                .txId(response.getId())
                .blockchain(response.getBlockchain())
                .status(response.getStatus())
                .createdAt(response.getCreatedAt())
                .build();

    }

    @Override
    public List<String> getUserWalletIds() {

        List<UserWalletJsonObject> getAllUserWallet =  Objects.requireNonNull(masterWalletRestTemplate.getForEntity(
                "/user-wallets",
                GetAllUserWalletJsonObject.class).getBody()).getResults();

        return getAllUserWallet.stream()
                .map(UserWalletJsonObject::getId)
                .collect(Collectors.toList());
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
                TransactionJsonObject.class).getBody();

        return Transaction.builder()
                .txId(response.getId())
                .blockchain(response.getBlockchain())
                .status(response.getStatus())
                .createdAt(response.getCreatedAt())
                .build();
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

        List<MasterWalletBalanceJsonObject> masterWalletBalanceJsonObjects = Arrays.asList(
                Objects.requireNonNull(masterWalletRestTemplate.getForEntity(
                        "/balance",
                        MasterWalletBalanceJsonObject[].class).getBody())
        );

        return Amount.of(masterWalletBalanceJsonObjects.stream()
                .filter(symbol -> symbol.getSymbol().equals(ticker)).findFirst().get().getSpendableAmount()
        );
    }




    public ValueTransferEventsJsonObject valueTransferEvent() {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();

        ValueTransferEventsJsonObject response = restTemplate.getForEntity(
                "/value-transfer-events",
                ValueTransferEventsJsonObject.class,
                param).getBody();

        System.out.println("Monitoring....");
        return response;
    }

}
