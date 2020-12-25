package io.haechi.henesis.assignment.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MasterWalletBalanceDto {
    @JsonProperty("coinId")
    private String coinId;
    @JsonProperty("coinType")
    private String coinType;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("decimals")
    private Integer decimals;
    @JsonProperty("spendableAmount")
    private String spendableAmount;
    @JsonProperty("name")
    private String name;
    @JsonProperty("symbol")
    private String symbol;

}
