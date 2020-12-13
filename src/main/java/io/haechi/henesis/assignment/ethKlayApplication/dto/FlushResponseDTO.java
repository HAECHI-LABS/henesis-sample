package io.haechi.henesis.assignment.ethKlayApplication.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushResponseDTO {
    private String transactionId;
    private String blockchain;
    private String status;


    private FlushResponseDTO(
            String transactionId,
            String blockchain,
            String status
    ){
        this.transactionId=transactionId;
        this.blockchain=blockchain;
        this.status=status;
    }

    public static FlushResponseDTO of(
            String transactionId,
            String blockchain,
            String status
    ){
        return new FlushResponseDTO(
                transactionId,
                blockchain,
                status
        );
    }
}
