package io.haechi.henesis.assignment.application.dto;

import io.haechi.henesis.assignment.domain.Amount;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferResponseDTO {
    private String walletName;
    private Amount walletBalance;
    private Amount amount;

    @Builder
    public TransferResponseDTO(
            String walletName,
            Amount walletBalance,
            Amount amount
    ){
        this.walletName = walletName;
        this.walletBalance = walletBalance;
        this.amount = amount;
    }


    private static Double toDouble(BigInteger amount){
        return amount.divide(BigInteger.TEN.pow(18)).doubleValue();
    }
}
