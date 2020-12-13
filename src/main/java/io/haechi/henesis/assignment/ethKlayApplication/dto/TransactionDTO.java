package io.haechi.henesis.assignment.ethKlayApplication.dto;

import io.haechi.henesis.assignment.ethKlayDomain.Amount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionDTO {
    private String walletId;
    private String txId;
    private String status;
    private String transferType;
    private Amount amount;

    private TransactionDTO(String walletId, String txId, String status, String transferType, Amount amount){
        this.walletId = walletId;
        this.txId = txId;
        this.status = status;
        this.transferType = transferType;
        this.amount = amount;
    }

    public static TransactionDTO of(String walletId, String txId, String status, String transferType, Amount amount){
        return new TransactionDTO(walletId, txId, status, transferType, amount);
    }
}
