package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.arguments.CreateUserArguments;
import io.haechi.henesis.assignment.domain.MasterWalletBalance;
import io.haechi.henesis.assignment.domain.Transaction;
import io.haechi.henesis.assignment.domain.arguments.FlushArguments;
import io.haechi.henesis.assignment.domain.arguments.TransferArguments;
import io.haechi.henesis.assignment.domain.arguments.ValueTransferEventArguments;
import io.haechi.henesis.assignment.infra.dto.*;
import io.haechi.henesis.assignment.domain.UserWallet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class HenesisApiCallService {
    private final RestTemplate masterWalletRestTemplate;
    private final RestTemplate restTemplate;
    private final String masterWalletId;
    private final String walletPassphrase;

    public HenesisApiCallService(
            @Qualifier("walletClient") RestTemplate restTemplate,
            @Qualifier("masterWalletClient") RestTemplate masterWalletRestTemplate,
            @Qualifier("masterWalletId") String masterWalletId,
            @Qualifier("passphrase") String walletPassphrase
    ){
        this.restTemplate = restTemplate;
        this.masterWalletRestTemplate = masterWalletRestTemplate;
        this.masterWalletId = masterWalletId;
        this.walletPassphrase = walletPassphrase;
    }

    /**
     * 사용자 지갑 생성하기 API Call
     * @param request
     * @return UserWallet
     */
    public UserWallet createUserWallet(CreateUserArguments request){

        UserWalletJsonObject response = masterWalletRestTemplate.postForEntity(
                "/user-wallets",
                request,
                UserWalletJsonObject.class).getBody();
        System.out.println("Create Wallet Response : "+response);


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
     * @param request
     * @return Transaction
     */
    public Transaction transfer(TransferArguments request) {

        TransactionJsonObject response = masterWalletRestTemplate.postForEntity(
                "/transfer",
                request,
                TransactionJsonObject.class).getBody();
        System.out.println("Transfer Response : "+response);

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
     * @param ticker
     * @return Optional<MasterWalletBalance>
     */
    public Optional<MasterWalletBalance> getMasterWalletBalance(String ticker){

        List<MasterWalletBalanceJsonObject> masterWalletBalanceJsonObjects = Arrays.asList(
                Objects.requireNonNull(masterWalletRestTemplate.getForEntity(
                        "/balance",
                        MasterWalletBalanceJsonObject[].class).getBody())
        );

        return masterWalletBalanceJsonObjects.stream().filter(symbol -> symbol.getSymbol().equals(ticker))
                .map(response ->
                    MasterWalletBalance.builder()
                        .coinId(response.getCoinId())
                        .coinType(response.getCoinType())
                        .amount(response.getAmount())
                        .decimals(response.getDecimals())
                        .spendableAmount(response.getSpendableAmount())
                        .name(response.getName())
                        .symbol(response.getSymbol())
                        .build())
                .findFirst();
    }

    /**
     * 특정 마스터 지갑의 전체 사용자 지갑 목록 조회하기 API Call
     * @return GetAllUserWalletJsonObject
     */
    public GetAllUserWalletJsonObject getAllUserWallet(){
        return masterWalletRestTemplate.getForEntity(
                "/user-wallets",
                GetAllUserWalletJsonObject.class).getBody();
    }


    /**
     * 사용자 지갑 잔액을 모두 끌어오기 API Call
     * @param request
     * @return Transaction
     */
    public Transaction flush(FlushArguments request){
        TransactionJsonObject response = masterWalletRestTemplate.postForEntity(
                "/flush",
                request,
                TransactionJsonObject.class).getBody();
        System.out.println("Flush Response : "+response);

        return Transaction.builder()
                .txId(response.getId())
                .blockchain(response.getBlockchain())
                .status(response.getStatus())
                .createdAt(response.getCreatedAt())
                .build();
    }

    public ValueTransferEventsJsonObject valueTransferEvent(ValueTransferEventArguments request){

        ValueTransferEventsJsonObject response = restTemplate.getForEntity(
                "/value-transfer-events",
                ValueTransferEventsJsonObject.class,
                request).getBody();

        System.out.println("Monitoring....");
        return response;
    }

}
