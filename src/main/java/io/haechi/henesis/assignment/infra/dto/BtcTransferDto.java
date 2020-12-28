package io.haechi.henesis.assignment.infra.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransferDto {
    private String id;
    private String hash;
    private String blockNumber;
    private String status;
    private String amount;
    private String receivedAt;
    private String sendTo;
    private String feeAmount;
    private String createdAt;
    private String type;
    private String hex;
    private TransactionDto transaction;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TransactionDto {
        private String id;
        private List<BtcTransactionOutputDto> outputs;
    }
}
