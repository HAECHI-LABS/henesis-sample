package io.haechi.henesis.assignment.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferResponse {
    private String name;
    private String amount;

    private TransferResponse(
            String name,
            String amount
    ) {
        this.name = name;
        this.amount = amount;
    }

    public static TransferResponse of(
            String name,
            String amount
    ) {
        return new TransferResponse(
                name,
                amount
        );
    }


}
