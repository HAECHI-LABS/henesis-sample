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
public class BtcTransactionOutputDto {
    @JsonProperty("outputIndex")
    private int outputIndex;
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("address")
    private String address;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("isChange")
    private boolean isChange;
    @JsonProperty("scriptPubKey")
    private String scriptPubKey;
}
