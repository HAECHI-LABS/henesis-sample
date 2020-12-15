package io.haechi.henesis.assignment.application.btc.dto;

import io.haechi.henesis.assignment.domain.btc.BtcAmount;
import io.haechi.henesis.assignment.domain.ethklay.Amount;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransferResponse {
    private String name;
    private BtcAmount amount;
    private BtcAmount balance;


    private BtcTransferResponse(
            String name,
            BtcAmount amount,
            BtcAmount balance
    ){
        this.name=name;
        this.amount =amount;
        this.balance =balance;
    }

    public static BtcTransferResponse of(String name, BtcAmount amount, BtcAmount balance) {
        return new BtcTransferResponse(
                name,
                amount,
                balance
        );
    }
}
