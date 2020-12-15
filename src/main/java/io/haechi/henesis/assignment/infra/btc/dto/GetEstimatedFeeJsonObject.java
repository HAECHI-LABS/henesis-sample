package io.haechi.henesis.assignment.infra.btc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetEstimatedFeeJsonObject {

    @JsonProperty("estimatedFee")
    private String estimatedFee;
}
