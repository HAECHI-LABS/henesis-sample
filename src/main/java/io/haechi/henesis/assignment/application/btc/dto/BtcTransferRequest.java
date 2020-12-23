package io.haechi.henesis.assignment.application.btc.dto;

import io.haechi.henesis.assignment.domain.ethklay.Amount;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BtcTransferRequest {

    @NotNull(message = "From Address must be not null")
    @NotEmpty(message = "From Address must be not empty")
    private String from;

    @NotNull(message = "To Address must be not null")
    @NotEmpty(message = "To Address must be not empty")
    private String to;

    @NotNull(message = "Amount must be not null")
    @NotEmpty(message = "Amount must be not empty")
    private Amount amount;


}
