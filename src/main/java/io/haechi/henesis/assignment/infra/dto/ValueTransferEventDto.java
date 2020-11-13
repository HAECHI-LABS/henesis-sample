package io.haechi.henesis.assignment.infra.dto;

import io.haechi.henesis.assignment.domain.Transfer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValueTransferEventDto {
    private Long id;
    private String from;
    private String to;
    private String amount;
    private String blockchain;
    private String status;
    private String coinSymbol;
    private String transferType;
    private String hash;
    private String walletType;
    private String transactionId;
    private String createdAt;
    private String updatedAt;

    public String getId() {
        return Long.toString(this.id);
    }

    public Transfer.Owner resolveOwner() {
        if (this.walletType.equals("MASTER_WALLET")) {
            return Transfer.Owner.MASTER_WALLET;
        }
        return Transfer.Owner.DEPOSIT_ADDRESS;
    }
}
