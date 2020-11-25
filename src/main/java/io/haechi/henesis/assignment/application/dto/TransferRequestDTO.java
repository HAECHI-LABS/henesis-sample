package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferRequestDTO {

    @JsonProperty("userWalletId")
    private String userWalletId;

    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("to")
    private String to;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("passphrase")
    private String passphrase;
}
