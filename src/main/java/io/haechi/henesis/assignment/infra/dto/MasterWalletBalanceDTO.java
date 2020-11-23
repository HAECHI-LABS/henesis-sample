package io.haechi.henesis.assignment.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.haechi.henesis.assignment.config.util.HexStringToBigInteger;
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
    @JsonDeserialize(using= HexStringToBigInteger.class)
    @JsonProperty("amount")
    private BigInteger amount;
    @JsonProperty("decimals")
    private Integer decimals;
    @JsonDeserialize(using= HexStringToBigInteger.class)
    @JsonProperty("spendableAmount")
    private BigInteger spendableAmount;
    @JsonDeserialize(using= HexStringToBigInteger.class)
    @JsonProperty("aggregatedAmount")
    private BigInteger aggregatedAmount;
    @JsonProperty("name")
    private String name;
    @JsonProperty("symbol")
    private String symbol;


}
