package io.haechi.henesis.assignment.infra.btc.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransactionJsonObject {
    private String id;
    private String transactionHash;
    private String blockNumber;
    private String amount;
    private String feeAmount;
    private String createdAt;
    private String updatedAt;
    private String hex;
    private BtcTransactionOutputJsonObject outputs;
}
