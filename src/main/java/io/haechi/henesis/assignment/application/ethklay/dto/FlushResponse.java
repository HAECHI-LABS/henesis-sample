package io.haechi.henesis.assignment.application.ethklay.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushResponse {
    private String transactionId;
    private String blockchain;
    private String status;


    private FlushResponse(
            String transactionId,
            String blockchain,
            String status
    ) {
        this.transactionId = transactionId;
        this.blockchain = blockchain;
        this.status = status;
    }

    public static FlushResponse of(
            String transactionId,
            String blockchain,
            String status
    ) {
        return new FlushResponse(
                transactionId,
                blockchain,
                status
        );
    }
}
