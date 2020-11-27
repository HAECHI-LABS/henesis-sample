package io.haechi.henesis.assignment.application.dto;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferResponseDTO {
    private String walletName;
    private Double walletBalance;
    private Double amount;

    @Builder
    public TransferResponseDTO(
            String walletName,
            BigInteger walletBalance,
            BigInteger amount
    ){
        this.walletName = walletName;
        this.walletBalance = toDouble(walletBalance);
        this.amount = toDouble(amount);
    }


    private static Double toDouble(BigInteger amount){
        return amount.divide(BigInteger.TEN.pow(18)).doubleValue();
    }
}
