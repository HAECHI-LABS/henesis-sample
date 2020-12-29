package io.haechi.henesis.assignment.infra.dto;

import io.haechi.henesis.assignment.domain.Transfer;
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

    public Transfer.Owner resolveOwner(String masterWalletAddress) {
        String address = this.receivedAt != null
                ? this.receivedAt
                : this.sendTo;
        if (address.equals(masterWalletAddress)) {
            return Transfer.Owner.MASTER_WALLET;
        }
        return Transfer.Owner.DEPOSIT_ADDRESS;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TransactionDto {
        private String id;
        private List<BtcTransactionOutputDto> outputs;
    }
}
