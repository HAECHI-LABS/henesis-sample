package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.haechi.henesis.assignment.domain.Amount;
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
    private Amount amount;
}
