package io.haechi.henesis.assignment.infra.btc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransactionJsonObject {
    @JsonProperty("id")
    private String id;
    @JsonProperty("transactionHash")
    private String transactionHash;
    @JsonProperty("blockNumber")
    private String blockNumber;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("feeAmount")
    private String feeAmount;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("hex")
    private String hex;
    @JsonProperty("outputs")
    private List<BtcTransactionOutputJsonObject> outputs;
}
