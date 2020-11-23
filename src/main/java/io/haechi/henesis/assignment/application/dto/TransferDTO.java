package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.haechi.henesis.assignment.config.util.BigIntegerToHexString;
import io.haechi.henesis.assignment.config.util.HexStringToBigInteger;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferDTO {

    @JsonProperty("userWalletId")
    private String userWalletId;

    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("to")
    private String to;
    @JsonDeserialize(using= HexStringToBigInteger.class)
    @JsonProperty("amount")
    private BigInteger amount;
    @JsonProperty("passphrase")
    private String passphrase;
}
