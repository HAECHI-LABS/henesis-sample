package io.haechi.henesis.assignment.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultsJsonObject {
    @JsonProperty("id")
    private int id;
    @JsonProperty("from")
    private String from;
    @JsonProperty("to")
    private String to;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("blockchain")
    private String blockchain;
    @JsonProperty("status")
    private String status;
    @JsonProperty("decimals")
    private int decimals;
    @JsonProperty("walletId")
    private String walletId;
    @JsonProperty("orgId")
    private String orgId;
    @JsonProperty("masterWalletId")
    private String masterWalletId;
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("coinSymbol")
    private String coinSymbol;
    @JsonProperty("confirmation")
    private String confirmation;
    @JsonProperty("transferType")
    private String transferType;
    @JsonProperty("transactionHash")
    private String transactionHash;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("walletName")
    private String walletName;
    @JsonProperty("walletType")
    private String walletType;

}
