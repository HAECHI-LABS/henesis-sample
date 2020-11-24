package io.haechi.henesis.assignment.domain;

import io.haechi.henesis.assignment.domain.util.Converter;
import lombok.*;

import java.math.BigInteger;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MasterWalletBalance extends Converter {
    private String coinId;
    private String coinType;
    private Integer decimals;
    private String amount;
    private String spendableAmount;
    private String name;
    private String symbol;


    /*
    public MasterWalletBalance(String coinId, String coinType, Integer decimals, String amount, String spendableAmount, String aggregatedAmount, String name, String symbol){
        this.coinId = coinId;
        this.coinType = coinType;
        this.decimals = decimals;
        this.amount = hexStringToBigInteger(amount, decimals);
        this.spendableAmount = hexStringToBigInteger(spendableAmount, decimals);
        this.aggregatedAmount = hexStringToBigInteger(aggregatedAmount, decimals);
        this.name = name;
        this.symbol = symbol;
    }
    */
}
