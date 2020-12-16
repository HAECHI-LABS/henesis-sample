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

    private BtcTransferResponse(
            String name,
            BtcAmount amount
    ){
        this.name=name;
        this.amount =amount;
    }

    public static BtcTransferResponse of(String name, BtcAmount amount) {
        return new BtcTransferResponse(
                name,
                amount
        );
    }
}
