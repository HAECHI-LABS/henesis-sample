package io.haechi.henesis.assignment.application.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValueTransferEventDTO {
    private String txHash;
    private String from;
    private String to;
    private String amount;
    private String status;

}
