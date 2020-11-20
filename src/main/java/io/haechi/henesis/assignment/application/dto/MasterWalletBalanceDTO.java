package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
    private String amount;
    @JsonProperty("decimals")
    private String decimals;
    @JsonProperty("spendableAmount")
    private String spendableAmount;
    @JsonProperty("aggregatedAmount")
    private String aggregatedAmount;
    @JsonProperty("name")
    private String name;
    @JsonProperty("symbol")
    private String symbol;


}
