package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MasterWalletBalanceDTO {
    @JsonProperty("coinId")
    private String coinId;
    @JsonProperty("coinType")
    private String coinType;
    @JsonProperty("amount")
    private BigInteger amount;
    @JsonProperty("decimals")
    private Integer decimals;
    @JsonProperty("spendableAmount")
    private BigInteger spendableAmount;
    @JsonProperty("name")
    private String name;
    @JsonProperty("symbol")
    private String symbol;


}
