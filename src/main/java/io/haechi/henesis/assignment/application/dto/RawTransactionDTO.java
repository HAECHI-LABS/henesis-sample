package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RawTransactionDTO {

    @JsonProperty("nonce")
    private String nonce;
    @JsonProperty("to")
    private String to;
    @JsonProperty("value")
    private String value;
    @JsonProperty("data")
    private String data;
    @JsonProperty("gasPrice")
    private String gasPrice;
    @JsonProperty("gasLimit")
    private String gasLimit;

}
