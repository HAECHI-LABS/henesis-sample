package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.haechi.henesis.assignment.domain.Amount;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransferRequest {
    private String symbol;
    private String to;
    private Amount amount;
}
