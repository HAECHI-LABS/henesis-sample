package io.haechi.henesis.assignment.application.dto;

import io.haechi.henesis.assignment.domain.Amount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferResponseDTO {
    private String walletName;
    private Double amount;
    private Double walletBalance;


    private TransferResponseDTO(
            String walletName,
            Amount amount,
            Amount walletBalance
    ){
        this.walletName = walletName;
        this.amount = amount.toDouble();
        this.walletBalance = walletBalance.toDouble();
    }

    public static TransferResponseDTO of(
            String walletName,
            Amount amount,
            Amount walletBalance
    ){
        return new TransferResponseDTO(
                walletName,
                amount,
                walletBalance
        );
    }


}
