package io.haechi.henesis.assignment.infra.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HenesisTransferRequest {
    private String amount;
    private String to;
    private String ticker;
    private String passphrase;

    public HenesisTransferRequest(String amount, String to, String passphrase) {
        this.amount = amount;
        this.to = to;
        this.passphrase = passphrase;
    }
}
