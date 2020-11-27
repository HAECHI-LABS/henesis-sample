package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FlushRequestDTO {
    @JsonProperty("ticker")
    private String ticker;

    @JsonProperty("passphrase")
    private String passphrase;

}
