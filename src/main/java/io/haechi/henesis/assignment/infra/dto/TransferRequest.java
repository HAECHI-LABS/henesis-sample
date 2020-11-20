package io.haechi.henesis.assignment.infra.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferRequest {
    private String ticker;
    private String to;
    private String amount;
    private String passphrase;
}
