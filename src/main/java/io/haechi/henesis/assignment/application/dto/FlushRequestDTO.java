package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.haechi.henesis.assignment.infra.dto.ResultsJsonObject;
import lombok.*;

import java.util.List;

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
