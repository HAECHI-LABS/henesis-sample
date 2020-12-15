package io.haechi.henesis.assignment.application.ethklay.dto;

import io.haechi.henesis.assignment.domain.ethklay.Amount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferResponse {
    private String walletName;
    private Double amount;
    private Double walletBalance;


    private TransferResponse(
            String walletName,
            Amount amount,
            Amount walletBalance
    ){
        this.walletName = walletName;
        this.amount = amount.toDouble();
        this.walletBalance = walletBalance.toDouble();
    }

    public static TransferResponse of(
            String walletName,
            Amount amount,
            Amount walletBalance
    ){
        return new TransferResponse(
                walletName,
                amount,
                walletBalance
        );
    }


}
