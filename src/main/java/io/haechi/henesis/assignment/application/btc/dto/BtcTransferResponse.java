package io.haechi.henesis.assignment.application.btc.dto;

import io.haechi.henesis.assignment.domain.Amount;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransferResponse {
    private String name;
    private Amount amount;
    private Amount balance;


    private BtcTransferResponse(
            String name,
            Amount amount,
            Amount balance
    ){
        this.name=name;
        this.amount =amount;
        this.balance =balance;
    }

    public static BtcTransferResponse of(String name, Amount amount, Amount balance) {
        return new BtcTransferResponse(
                name,
                amount,
                balance
        );
    }
}
