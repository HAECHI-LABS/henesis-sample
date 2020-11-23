package io.haechi.henesis.assignment.application.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferCoinDTO {
    private String txId;
    private String blockchain;
    private String status;

}
