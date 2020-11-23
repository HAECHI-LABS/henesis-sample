package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawRequestDTO {

    @JsonProperty("userWalletId")
    private String userWalletId;

    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("to")
    private String to;
    @JsonProperty("amount")
    private BigInteger amount;
    @JsonProperty("passphrase")
    private String passphrase;
}
