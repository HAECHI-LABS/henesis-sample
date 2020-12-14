package io.haechi.henesis.assignment.infra.ethklay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushRequestJsonObject {
    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("userWalletIds")
    private String userWalletIds;
    @JsonProperty("passphrase")
    private String passphrase;
}
