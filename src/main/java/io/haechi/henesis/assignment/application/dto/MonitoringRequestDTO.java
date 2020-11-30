package io.haechi.henesis.assignment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonitoringRequestDTO {

    @JsonProperty("ticker")
    private String ticker;
}
