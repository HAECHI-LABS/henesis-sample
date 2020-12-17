package io.haechi.henesis.assignment.application.btc.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransferResponse {
    private String name;
    private String amount;

    private BtcTransferResponse(
            String name,
            String amount
    ) {
        this.name = name;
        this.amount = amount;
    }

    public static BtcTransferResponse of(String name, String amount) {
        return new BtcTransferResponse(
                name,
                amount
        );
    }
}
