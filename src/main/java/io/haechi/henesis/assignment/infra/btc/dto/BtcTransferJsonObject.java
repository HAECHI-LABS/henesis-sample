package io.haechi.henesis.assignment.infra.btc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransferJsonObject {
    @JsonProperty("id")
    private String id;
    @JsonProperty("walletId")
    private String walletId;
    @JsonProperty("outputIndex")
    private Integer outputIndex;
    @JsonProperty("transaction")
    private BtcTransactionJsonObject transaction;
    @JsonProperty("receivedAt")
    private String receivedAt;
    @JsonProperty("sendTo")
    private String sendTo;
    @JsonProperty("feeAmount")
    private String feeAmount;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("withdrawalApprovalId")
    private String withdrawalApprovalId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("status")
    private String status;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;

}
