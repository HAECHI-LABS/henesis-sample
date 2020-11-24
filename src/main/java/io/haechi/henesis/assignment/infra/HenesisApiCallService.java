package io.haechi.henesis.assignment.infra;

import io.haechi.henesis.assignment.domain.arguments.CreateUserArguments;
import io.haechi.henesis.assignment.domain.MasterWalletBalance;
import io.haechi.henesis.assignment.domain.Transaction;
import io.haechi.henesis.assignment.domain.arguments.TransferArguments;
import io.haechi.henesis.assignment.infra.dto.*;
import io.haechi.henesis.assignment.domain.UserWallet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
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
            @Qualifier("walletPassphrase") String walletPassphrase
    ){
        this.restTemplate = restTemplate;
        this.masterWalletRestTemplate = masterWalletRestTemplate;
        this.masterWalletId = masterWalletId;
        this.walletPassphrase = walletPassphrase;
    }

    public UserWallet createUserWallet(CreateUserArguments request){

        UserWalletJsonObject response = masterWalletRestTemplate.postForEntity(
                "/user-wallets",
                CreateUserArguments.builder()
                        .name(request.getName().trim())
                        .passphrase(request.getPassphrase().trim())
                        .build(),
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
    public Transaction transfer(TransferArguments request) {

        System.out.println("Amount : "+request.getAmount());

        TransactionJsonObject response = masterWalletRestTemplate.postForEntity(
                "/transfer",
                request,
                TransactionJsonObject.class).getBody();
        System.out.println("Transfer Response : "+response);

        return Transaction.builder()
                .id(response.getId())
                .blockchain(response.getBlockchain())
                .status(response.getStatus())
                .createdAt(response.getCreatedAt())
                .build();

    }
    public Optional<MasterWalletBalance> getMasterWalletBalance(String ticker){

        List<MasterWalletBalanceJsonObject> masterWalletBalanceJsonObjects = Arrays.asList(masterWalletRestTemplate.getForEntity(
                "/balance",
                MasterWalletBalanceJsonObject[].class).getBody()
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

    /*

            return TransferResponse.builder()
                .id(response.getId())
                .blockchain(response.getBlockchain())
                .status(response.getStatus())
                .build();

    public FlushedTx flush(String ticker, String userWalletIds, String passphrase){

    }
    */


}
